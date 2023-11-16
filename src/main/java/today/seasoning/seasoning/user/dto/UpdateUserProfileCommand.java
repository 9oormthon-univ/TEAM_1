package today.seasoning.seasoning.user.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UpdateUserProfileCommand {

	private final Long userId;
	private final String accountId;
	private final String nickname;
	private final MultipartFile profileImage;

	public UpdateUserProfileCommand(Long userId, String accountId, String nickname,
		MultipartFile profileImage) {
		this.userId = userId;
		this.accountId = accountId;
		this.nickname = nickname;
		this.profileImage = profileImage;
	}
}
