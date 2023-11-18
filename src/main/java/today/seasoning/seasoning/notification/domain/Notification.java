package today.seasoning.seasoning.notification.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.NoArgsConstructor;
import today.seasoning.seasoning.common.util.TsidUtil;
import today.seasoning.seasoning.user.domain.User;

@Entity
@NoArgsConstructor
public class Notification {

	@Id
	private Long id;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@Enumerated(EnumType.STRING)
	private NotificationType type;

	private String message;

	@Column(name = "is_read")
	private boolean isRead;

	private Notification(Long id, User user, NotificationType type, String message,
		boolean isRead) {
		this.id = id;
		this.user = user;
		this.type = type;
		this.message = message;
	}

	public static Notification create(NotificationType type, User user, String message) {
		return new Notification(TsidUtil.createLong(),
			user,
			type,
			message,
			false);
	}
}
