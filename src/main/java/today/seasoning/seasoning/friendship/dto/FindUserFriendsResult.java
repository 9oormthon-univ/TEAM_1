package today.seasoning.seasoning.friendship.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindUserFriendsResult {

	private final String nickname;
	private final String accountId;
	private final String profileImageUrl;
}
