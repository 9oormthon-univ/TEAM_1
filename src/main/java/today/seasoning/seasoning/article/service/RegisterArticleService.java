package today.seasoning.seasoning.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.article.domain.Article;
import today.seasoning.seasoning.article.domain.ArticleImage;
import today.seasoning.seasoning.article.domain.ArticleImageRepository;
import today.seasoning.seasoning.article.domain.ArticleRepository;
import today.seasoning.seasoning.article.dto.ArticleImageDto;
import today.seasoning.seasoning.article.dto.RegisterArticleCommand;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.common.util.SolarTermUtil;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterArticleService {

	private final UserRepository userRepository;
	private final ArticleRepository articleRepository;
	private final ArticleImageRepository articleImageRepository;

	public Long doRegister(RegisterArticleCommand command) {
		verifySolarTerm();

		Article article = createArticle(command);

		articleRepository.save(article);
		registerArticleImages(article, command);

		return article.getId();
	}

	private void verifySolarTerm() {
		if (SolarTermUtil.getCurrentTerm() == -1) {
			throw new CustomException(HttpStatus.FORBIDDEN, "기록장이 열리지 않았습니다.");
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

	private void registerArticleImages(Article article, RegisterArticleCommand command) {
		command.getArticleImages()
			.stream()
			.map(dto -> createArticleImage(article, dto))
			.forEach(articleImageRepository::save);
	}

	private ArticleImage createArticleImage(Article article, ArticleImageDto dto) {
		return new ArticleImage(article,
			dto.getUrl(),
			dto.getSequence());
	}

}
