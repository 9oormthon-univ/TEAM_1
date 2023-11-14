package today.seasoning.seasoning.common;

import java.io.Serializable;
import lombok.Getter;
import today.seasoning.seasoning.common.enums.LoginType;
import today.seasoning.seasoning.user.domain.User;

@Getter
public class UserPrincipal implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Long id;
	private final String nickname;
	private final String profileImageUrl;
	private final String accountId;
	private final String email;
	private final LoginType loginType;

	public UserPrincipal(Long id, String nickname, String profileImageUrl, String accountId,
		String email, LoginType loginType) {
		this.id = id;
		this.nickname = nickname;
		this.profileImageUrl = profileImageUrl;
		this.accountId = accountId;
		this.email = email;
		this.loginType = loginType;
	}

	public static UserPrincipal builder(User user) {
		return new UserPrincipal(
			user.getId(),
			user.getNickname(),
			user.getProfileImageUrl(),
			user.getAccountId(),
			user.getEmail(),
			user.getLoginType());
	}
}
