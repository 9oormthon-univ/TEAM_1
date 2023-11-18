package today.seasoning.seasoning.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.notification.domain.Notification;
import today.seasoning.seasoning.notification.domain.NotificationRepository;
import today.seasoning.seasoning.notification.dto.RegisterNotificationCommand;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final UserRepository userRepository;
	private final NotificationRepository notificationRepository;

	@Transactional
	public void registerNotification(RegisterNotificationCommand command) {
		User user = findUserOrThrow(command.getUserId());

		Notification notification = Notification.create(command.getType(),
			user,
			command.getMessage());

		notificationRepository.save(notification);
	}

	private User findUserOrThrow(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "회원 조회 실패"));
	}
}
