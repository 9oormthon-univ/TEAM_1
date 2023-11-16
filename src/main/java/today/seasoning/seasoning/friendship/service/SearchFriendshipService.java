package today.seasoning.seasoning.friendship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.common.enums.FriendshipStatus;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.friendship.domain.Friendship;
import today.seasoning.seasoning.friendship.domain.FriendshipRepository;
import today.seasoning.seasoning.friendship.dto.SearchFriendResult;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;

@Service
@RequiredArgsConstructor
public class SearchFriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public SearchFriendResult doService(Long userId, String friendAccountId) {

        User friend = userRepository.findByAccountId(friendAccountId).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "친구 검색 실패"));

        SearchFriendResult searchFriendResult = new SearchFriendResult(friend.getNickname(),
                friend.getProfileImageUrl(),
                friendAccountId,
                FriendshipStatus.UNFRIEND);

        // 나 자신인 경우
        if (userId.equals(friend.getId())) {
            searchFriendResult.setFriendshipStatus(FriendshipStatus.SELF);
            return searchFriendResult;
        }

        Friendship forwardFriendship = friendshipRepository.findByUserIds(userId, friend.getId()).orElse(null);
        Friendship reverseFriendship = friendshipRepository.findByUserIds(friend.getId(), userId).orElse(null);

        // 친구 관계가 있는 경우
        if (forwardFriendship != null) {
            if (forwardFriendship.isValid()) {
                // 서로 친구인 상태
                if (reverseFriendship.isValid()) {
                    searchFriendResult.setFriendshipStatus(FriendshipStatus.FRIEND);
                } else { // 로그인한 사용자가 친구 요청한 상태
                    searchFriendResult.setFriendshipStatus(FriendshipStatus.SENT);
                }
            } else {
                // 로그인한 사용자가 친구 요청 받은 상태
                searchFriendResult.setFriendshipStatus(FriendshipStatus.RECEIVED);
            }
        }
        return searchFriendResult;
    }

}
