package today.seasoning.seasoning.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.article.domain.Article;
import today.seasoning.seasoning.article.domain.ArticleLike;
import today.seasoning.seasoning.article.domain.ArticleLikeRepository;
import today.seasoning.seasoning.article.domain.ArticleRepository;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleLikeService {

	private final UserRepository userRepository;
	private final ArticleRepository articleRepository;
	private final ArticleLikeRepository articleLikeRepository;

	public void doLike(Long userId, Long articleId) {
		User user = userRepository.findById(userId).get();

		Article article = articleRepository.findById(articleId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "기록장 조회 실패"));

		if (articleLikeRepository.findByArticleAndUser(articleId, userId).isPresent()) {
			throw new CustomException(HttpStatus.CONFLICT, "이미 좋아요를 눌렀습니다.");
		}

		ArticleLike articleLike = new ArticleLike(article, user);
		articleLikeRepository.save(articleLike);
	}
}
