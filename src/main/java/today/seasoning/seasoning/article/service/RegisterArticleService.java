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
import today.seasoning.seasoning.article.dto.RegisterArticleCommand;
import today.seasoning.seasoning.common.aws.S3Service;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.common.util.SolarTermUtil;
import today.seasoning.seasoning.common.util.TsidUtil;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterArticleService {

	private final S3Service s3Service;
	private final UserRepository userRepository;
	private final ArticleRepository articleRepository;
	private final ArticleImageRepository articleImageRepository;

	public Long doRegister(RegisterArticleCommand command) {
		verifySolarTerm();

		Article article = createArticle(command);
		articleRepository.save(article);

		uploadAndRegisterArticleImages(article, command.getImages());

		return article.getId();
	}

	private void verifySolarTerm() {
		if (SolarTermUtil.getCurrentTerm() == -1) {
			throw new CustomException(HttpStatus.FORBIDDEN, "등록기간이 아닙니다.");
		}
	}

	private Article createArticle(RegisterArticleCommand command) {
		User user = userRepository.findById(command.getUserId()).get();

		return new Article(user,
			command.isPublic(),
			SolarTermUtil.getCurrentYear(),
			SolarTermUtil.getCurrentTerm(),
			command.getContents());
	}

	private void uploadAndRegisterArticleImages(Article article, List<MultipartFile> images) {
		for (int sequence = 0; sequence < images.size(); sequence++) {
			MultipartFile image = images.get(sequence);
			String url = uploadImage(image);
			registerArticleImage(article, url, sequence + 1);
		}
	}

	private String uploadImage(MultipartFile image) {
		String prefix = TsidCreator.getTsid().encode(62);
		String filename = image.getOriginalFilename();
		return s3Service.uploadFile(image, prefix.concat(filename));
	}

	private void registerArticleImage(Article article, String url, int sequence) {
		ArticleImage articleImage = new ArticleImage(TsidUtil.createLong(), article, url, sequence);
		articleImageRepository.save(articleImage);
	}
}
