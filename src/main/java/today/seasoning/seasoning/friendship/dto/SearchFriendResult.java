package today.seasoning.seasoning.friendship.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import today.seasoning.seasoning.common.enums.FriendshipStatus;

@Getter
@RequiredArgsConstructor
public class SearchFriendResult {
    private final String nickname;
    private final String image;
    private final String accountId;
    private FriendshipStatus friendshipStatus;

    public SearchFriendResult(String nickname, String image, String accountId, FriendshipStatus friendshipStatus) {
        this.nickname = nickname;
        this.image = image;
        this.accountId = accountId;
        this.friendshipStatus = friendshipStatus;
    }

    public void setFriendshipStatus(FriendshipStatus friendshipStatus) {
        this.friendshipStatus = friendshipStatus;
    }
}
