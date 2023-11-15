package today.seasoning.seasoning.friendship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.friendship.domain.Friendship;
import today.seasoning.seasoning.friendship.domain.FriendshipRepository;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CancelFriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public void doService(Long cancellerId, String toUserAccountId) {

        User toUser = userRepository.findByAccountId(toUserAccountId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "상대 회원 조회 실패"));

        Friendship forwardFriendship = friendshipRepository.findByUserIds(cancellerId, toUser.getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "신청된 내역이 없습니다."));

        Friendship reverseFriendship = friendshipRepository.findByUserIds(toUser.getId(), cancellerId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "신청된 내역이 없습니다."));

        // 취소자 valid 1, 상대 valid 0
        if (forwardFriendship.isValid() && !reverseFriendship.isValid()) {
            friendshipRepository.delete(forwardFriendship);
            friendshipRepository.delete(reverseFriendship);
        } else {
            throw new CustomException(HttpStatus.BAD_REQUEST, "유효하지 않은 요청입니다.");
        }
    }
}
