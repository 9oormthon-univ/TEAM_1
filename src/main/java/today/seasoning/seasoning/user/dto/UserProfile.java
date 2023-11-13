package today.seasoning.seasoning.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserProfile {

	private final String nickname;
	private final String email;
	private final String profileImageUrl;
}
