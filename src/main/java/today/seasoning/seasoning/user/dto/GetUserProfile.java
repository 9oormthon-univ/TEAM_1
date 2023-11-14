package today.seasoning.seasoning.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import today.seasoning.seasoning.user.domain.User;

@Getter
@Setter
@RequiredArgsConstructor
public class GetUserProfile {

	private final String nickname;
	private final String profileImageUrl;
	private final String accountId;

	public GetUserProfile(User user) {
		this.nickname = user.getNickname();
		this.profileImageUrl = user.getProfileImageUrl();
		this.accountId = user.getAccountId();
	}
}
