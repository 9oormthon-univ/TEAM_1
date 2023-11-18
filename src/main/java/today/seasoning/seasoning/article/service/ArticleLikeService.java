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
import today.seasoning.seasoning.common.util.EntitySerializationUtil;
import today.seasoning.seasoning.friendship.service.port.in.CheckFriendshipValid;
import today.seasoning.seasoning.notification.domain.NotificationType;
import today.seasoning.seasoning.notification.dto.RegisterNotificationCommand;
import today.seasoning.seasoning.notification.service.NotificationService;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;
import today.seasoning.seasoning.user.dto.UserProfileDto;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleLikeService {

	private final UserRepository userRepository;
	private final ArticleRepository articleRepository;
	private final ArticleLikeRepository articleLikeRepository;
	private final CheckFriendshipValid checkFriendshipValid;
	private final NotificationService notificationService;

	public void doLike(Long userId, Long articleId) {
		User user = userRepository.findById(userId).get();

		Article article = findArticleOrThrow(articleId);

		validatePermission(userId, article);

		if (articleLikeRepository.findByArticleAndUser(articleId, userId).isPresent()) {
			throw new CustomException(HttpStatus.CONFLICT, "이미 좋아요를 눌렀습니다.");
		}

		ArticleLike articleLike = new ArticleLike(article, user);
		articleLikeRepository.save(articleLike);

		registerArticleFeedbackNotification(user, article.getUser());
	}

	public void cancelLike(Long userId, Long articleId) {
		Article article = findArticleOrThrow(articleId);

		validatePermission(userId, article);

		ArticleLike articleLike = articleLikeRepository.findByArticleAndUser(articleId, userId)
			.orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN, "좋아요를 누르지 않았습니다"));

		articleLikeRepository.delete(articleLike);
	}

	private Article findArticleOrThrow(Long articleId) {
		return articleRepository.findById(articleId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "기록장 조회 실패"));
	}

	private void registerArticleFeedbackNotification(User reader, User author) {
		UserProfileDto userProfile = UserProfileDto.build(reader);
		String userProfileJsonMessage = EntitySerializationUtil.serialize(userProfile);

		RegisterNotificationCommand command = new RegisterNotificationCommand(
			author.getId(),
			NotificationType.ARTICLE_FEEDBACK,
			userProfileJsonMessage);

		notificationService.registerNotification(command);
	}

	private void validatePermission(Long userId, Article article) {
		Long authorId = article.getUser().getId();

		// 자신의 글
		if (authorId.equals(userId)) {
			return;
		}
		// 공개된 친구의 글
		if (article.isPublished() && checkFriendshipValid.doCheck(userId, authorId)) {
			return;
		}

		throw new CustomException(HttpStatus.FORBIDDEN, "접근 권한 없음");
	}
}
