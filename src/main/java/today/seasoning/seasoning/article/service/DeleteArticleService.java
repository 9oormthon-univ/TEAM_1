package today.seasoning.seasoning.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.article.domain.Article;
import today.seasoning.seasoning.article.domain.ArticleRepository;
import today.seasoning.seasoning.common.exception.CustomException;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteArticleService {

	private final ArticleRepository articleRepository;

	public void doDelete(Long userId, Long articleId) {
		Article article = findArticle(articleId);

		validatePermission(userId, article);

		articleRepository.delete(article);
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
}