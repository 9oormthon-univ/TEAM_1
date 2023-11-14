package today.seasoning.seasoning.friendship.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import today.seasoning.seasoning.common.util.TsidUtil;
import today.seasoning.seasoning.user.domain.User;

@Entity
@Getter
@NoArgsConstructor
public class Friendship {

	@Id
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_user_id")
	private User fromUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_user_id")
	private User toUser;

	@Enumerated(value = EnumType.STRING)
	private FriendshipStatus status;

	public Friendship(User fromUser, User toUser) {
		this.id = TsidUtil.createLong();
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.status = FriendshipStatus.PENDING;
	}

	public void setAccepted() {
		this.status = FriendshipStatus.ACCEPTED;
	}
}
