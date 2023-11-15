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
@RequiredArgsConstructor
public class AcceptFriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Transactional
    public void doService(Long acceptorId, String requesterAccountId) {

        User requestUser = userRepository.findByAccountId(requesterAccountId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "상대 회원 조회 실패"));

        Friendship forwardFriendship = friendshipRepository.findByUserIds(acceptorId, requestUser.getId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "신청된 내역이 없습니다."));

        Friendship reverseFriendship = friendshipRepository.findByUserIds(requestUser.getId(), acceptorId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "신청된 내역이 없습니다."));

        // 수락자 valid 0, 상대 valid 1
        if (!forwardFriendship.isValid() && reverseFriendship.isValid()) {
            forwardFriendship.setValid();
            friendshipRepository.save(forwardFriendship);
        } else {
            throw new CustomException(HttpStatus.FORBIDDEN, "유효하지 않은 요청입니다.");
        }
    }
}
