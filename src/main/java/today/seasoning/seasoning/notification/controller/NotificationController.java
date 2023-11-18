package today.seasoning.seasoning.notification.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import today.seasoning.seasoning.common.UserPrincipal;
import today.seasoning.seasoning.common.util.TsidUtil;
import today.seasoning.seasoning.notification.dto.FindNotificationCommand;
import today.seasoning.seasoning.notification.dto.NotificationDto;
import today.seasoning.seasoning.notification.service.NotificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping
	public ResponseEntity<List<NotificationDto>> findNotifications(
		@AuthenticationPrincipal UserPrincipal principal,
		@RequestParam(name = "lastId", defaultValue = "AzL8n0Y58m7") String lastReadNotificationId,
		@RequestParam(name = "size", defaultValue = "10") Integer pageSize) {

		FindNotificationCommand findNotificationCommand = new FindNotificationCommand(
			principal.getId(),
			TsidUtil.toLong(lastReadNotificationId),
			pageSize);

		List<NotificationDto> notifications = notificationService.findNotifications(
			findNotificationCommand);

		return ResponseEntity.ok(notifications);
	}

}
