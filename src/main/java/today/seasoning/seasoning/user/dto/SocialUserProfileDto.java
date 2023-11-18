package today.seasoning.seasoning.user.dto;

import lombok.Getter;

@Getter
public class SocialUserProfileDto {

	private final String nickname;
	private final String email;
	private final String profileImageUrl;

	public SocialUserProfileDto(String nickname, String email, String profileImageUrl) {
		this.nickname = nickname;
		this.email = email;
		this.profileImageUrl = profileImageUrl;
	}
}
