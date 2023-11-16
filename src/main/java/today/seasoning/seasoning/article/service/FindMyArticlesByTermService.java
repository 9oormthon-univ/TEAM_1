package today.seasoning.seasoning.article.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.article.domain.Article;
import today.seasoning.seasoning.article.domain.ArticleImage;
import today.seasoning.seasoning.article.domain.ArticleRepository;
import today.seasoning.seasoning.common.util.TsidUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindMyArticlesByTermService {

	private final ArticleRepository articleRepository;

	public List<FindMyArticlesByTermResult> doFind(Long userId, int term) {
		return articleRepository.findByUserIdAndTerm(userId, term)
			.stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	private FindMyArticlesByTermResult toDto(Article article) {
		String contentsPreview = getContentsPreview(article.getContents());

		String thumbnailImageUrl = getFirstImageUrl(article.getArticleImages());

		return new FindMyArticlesByTermResult(
			TsidUtil.toString(article.getId()),
			article.getCreatedYear(),
			contentsPreview,
			thumbnailImageUrl);
	}

	private String getFirstImageUrl(List<ArticleImage> images) {
		return images
			.stream()
			.min(Comparator.comparingInt(ArticleImage::getSequence))
			.map(ArticleImage::getUrl)
			.orElse(null);
	}

	private String getContentsPreview(String contents) {
		int previewLength = Math.min(contents.length(), 150);
		return contents.substring(0, previewLength);
	}
}
