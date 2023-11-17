package today.seasoning.seasoning.article.dto;

import lombok.Getter;
import today.seasoning.seasoning.user.domain.User;

@Getter
public class UserProfileDto {

	private final String nickname;
	private final String accountID;
	private final String image;

	public UserProfileDto(String nickname, String accountID, String image) {
		this.nickname = nickname;
		this.accountID = accountID;
		this.image = image;
	}

	public static UserProfileDto build(User user) {
		return new UserProfileDto(
			user.getNickname(),
			user.getAccountId(),
			user.getProfileImageUrl());
	}
}
