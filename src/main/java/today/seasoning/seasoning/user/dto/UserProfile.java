package today.seasoning.seasoning.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import today.seasoning.seasoning.user.domain.User;

@Getter
@RequiredArgsConstructor
public class UserProfile {

	private final String nickname;
	private final String email;
	private final String profileImageUrl;

	public UserProfile(User user) {
		this.nickname = user.getNickname();
		this.email = user.getEmail();
		this.profileImageUrl = user.getProfileImageUrl();
	}
}
