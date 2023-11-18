package today.seasoning.seasoning.notification.dto;

import lombok.Getter;
import today.seasoning.seasoning.notification.domain.NotificationType;

@Getter
public class RegisterNotificationCommand {

	private final Long userId;
	private final NotificationType type;
	private final String message;

	public RegisterNotificationCommand(Long userId, NotificationType type, String message) {
		this.userId = userId;
		this.type = type;
		this.message = message;
	}
}
