package today.seasoning.seasoning.notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import today.seasoning.seasoning.common.util.TsidUtil;
import today.seasoning.seasoning.notification.domain.Notification;
import today.seasoning.seasoning.notification.domain.NotificationType;

@Getter
@NoArgsConstructor
public class NotificationDto {

	private String id;
	private NotificationType type;
	private String message;
	private boolean isRead;

	private NotificationDto(String id, NotificationType type, String message, boolean isRead) {
		this.id = id;
		this.type = type;
		this.message = message;
		this.isRead = isRead;
	}

	public static NotificationDto build(Notification notification) {
		return new NotificationDto(
			TsidUtil.toString(notification.getId()),
			notification.getType(),
			notification.getMessage(),
			notification.isRead());
	}
}
