package today.seasoning.seasoning.notification.dto;

import lombok.Getter;

@Getter
public class FindNotificationCommand {

	private final Long userId;
	private final Long lastReadNotificationId;
	private final int pageSize;

	public FindNotificationCommand(Long userId, Long lastReadNotificationId, int pageSize) {
		this.userId = userId;
		this.lastReadNotificationId = lastReadNotificationId;
		this.pageSize = pageSize;
	}
}
