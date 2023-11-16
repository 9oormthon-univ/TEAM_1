package today.seasoning.seasoning.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.article.domain.Article;
import today.seasoning.seasoning.article.domain.ArticleImage;
import today.seasoning.seasoning.article.domain.ArticleImageRepository;
import today.seasoning.seasoning.article.domain.ArticleRepository;
import today.seasoning.seasoning.article.dto.UpdateArticleDto;
import today.seasoning.seasoning.article.dto.UpdateArticleImageDto;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.common.util.TsidUtil;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;
    private final EntityManager entityManager;

    public void doUpdate(Long userId, Long articleId, UpdateArticleDto command) {
        Article article = findArticle(articleId);

        validatePermission(userId, article);

        List<ArticleImage> updatedArticleImages = getUpdatedArticleImages(article, command.getImages());

        article.update(command.getIsPublic(), command.getContents(), updatedArticleImages);
        articleRepository.save(article);
    }

    private Article findArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "기록장 조회 실패"));
    }

    private void validatePermission(Long userId, Article article) {
        Long authorId = article.getUser().getId();

        if (!authorId.equals(userId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "권한 없음");
        }
    }

    private List<ArticleImage> getUpdatedArticleImages(Article article, List<UpdateArticleImageDto> imageDtos) {
        List<ArticleImage> updatedImages = new ArrayList<>();

        for (UpdateArticleImageDto imageDto : imageDtos) {
            if (imageDto.getUpdated()) {
                ArticleImage newImage = createArticleImage(article, imageDto);
                articleImageRepository.save(newImage);
                updatedImages.add(newImage);
            } else {
                ArticleImage existingImage = findArticleImage(imageDto.getId());
                updatedImages.add(existingImage);
            }
        }
        return updatedImages;
    }

    private ArticleImage createArticleImage(Article article, UpdateArticleImageDto imageDto) {
        return new ArticleImage(article,
                imageDto.getUrl(),
                imageDto.getSequence());
    }

    private ArticleImage findArticleImage(String stringArticleImageId) {
        Long articleImageId = TsidUtil.toLong(stringArticleImageId);

        return articleImageRepository.findById(articleImageId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "이미지 조회 실패"));
    }
}
