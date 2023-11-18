package today.seasoning.seasoning.notification.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.notification.domain.Notification;
import today.seasoning.seasoning.notification.domain.NotificationRepository;
import today.seasoning.seasoning.notification.domain.NotificationType;
import today.seasoning.seasoning.notification.dto.FindNotificationCommand;
import today.seasoning.seasoning.notification.dto.NotificationDto;
import today.seasoning.seasoning.notification.dto.RegisterNotificationCommand;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

	private final UserRepository userRepository;
	private final EntityManager entityManager;
	private final NotificationRepository notificationRepository;

	public void registerNotification(RegisterNotificationCommand command) {
		User user = findUserOrThrow(command.getUserId());

		Notification notification = Notification.create(command.getType(),
			user,
			command.getMessage());

		notificationRepository.save(notification);
	}

	public List<NotificationDto> findNotifications(FindNotificationCommand command) {
		String sql = "SELECT n FROM Notification n WHERE n.user.id = :userId "
			+ "AND n.id < :notificationId "
			+ "ORDER BY n.id DESC";

		List<Notification> notificationList = entityManager.createQuery(sql, Notification.class)
			.setParameter("userId", command.getUserId())
			.setParameter("notificationId", command.getLastReadNotificationId())
			.setMaxResults(command.getPageSize())
			.getResultList();

		List<NotificationDto> notificationDtos = notificationList.stream()
			.map(NotificationDto::build)
			.collect(Collectors.toList());

		markNotificationsAsRead(notificationList);

		return notificationDtos;
	}

	public void registerArticleOpenNotification(int term) {
		userRepository.findAll()
			.stream()
			.map(u -> getRegisterNotificationCommand(u.getId(), NotificationType.ARTICLE_OPEN,
				String.valueOf(term)))
			.forEach(this::registerNotification);
	}

	private User findUserOrThrow(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "회원 조회 실패"));
	}

	private void markNotificationsAsRead(List<Notification> sentNotificationList) {
		sentNotificationList.stream()
			.forEach(Notification::markAsRead);

		notificationRepository.saveAll(sentNotificationList);
	}

	private RegisterNotificationCommand getRegisterNotificationCommand(Long userId,
		NotificationType type, String message) {
		return new RegisterNotificationCommand(userId, type, message);
	}
}
