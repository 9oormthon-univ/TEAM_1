package today.seasoning.seasoning.article.service;

import com.github.f4b6a3.tsid.TsidCreator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import today.seasoning.seasoning.article.domain.Article;
import today.seasoning.seasoning.article.domain.ArticleImage;
import today.seasoning.seasoning.article.domain.ArticleImageRepository;
import today.seasoning.seasoning.article.domain.ArticleRepository;
import today.seasoning.seasoning.article.dto.UpdateArticleCommand;
import today.seasoning.seasoning.common.aws.S3Service;
import today.seasoning.seasoning.common.aws.UploadFileInfo;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.common.util.TsidUtil;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateArticleService {

	private final S3Service s3Service;
	private final ArticleRepository articleRepository;
	private final ArticleImageRepository articleImageRepository;

	public void doUpdate(UpdateArticleCommand command) {
		Article article = findArticle(command.getArticleId());

		validatePermission(command.getUserId(), article);

		deleteOldImages(article.getArticleImages());

		uploadAndRegisterArticleImages(article, command.getImages());

		updateArticle(article, command);
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

	private void deleteOldImages(List<ArticleImage> articleImages) {
		articleImages.stream()
			.map(ArticleImage::getFilename)
			.forEach(s3Service::deleteFile);
		articleImages.clear();
	}

	private void uploadAndRegisterArticleImages(Article article, List<MultipartFile> images) {
		for (int sequence = 0; sequence < images.size(); sequence++) {
			MultipartFile image = images.get(sequence);
			UploadFileInfo uploadFileInfo = uploadImage(image);
			registerArticleImage(article, uploadFileInfo, sequence + 1);
		}
	}

	private UploadFileInfo uploadImage(MultipartFile image) {
		String uid = TsidCreator.getTsid().encode(62);
		String uploadFileName = "images/article/" + uid + "/" + image.getOriginalFilename();
		String url = s3Service.uploadFile(image, uploadFileName);
		return new UploadFileInfo(uploadFileName, url);
	}

	private void registerArticleImage(Article article, UploadFileInfo fileInfo, int sequence) {
		ArticleImage articleImage = new ArticleImage(TsidUtil.createLong(),
			article,
			fileInfo.getFilename(),
			fileInfo.getUrl(),
			sequence);
		articleImageRepository.save(articleImage);
	}

	private void updateArticle(Article article, UpdateArticleCommand command) {
		article.update(command.isPublished(), command.getContents());
		articleRepository.save(article);
	}
}