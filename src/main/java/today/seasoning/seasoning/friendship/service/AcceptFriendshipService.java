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
                .orElseThrow(()-> new CustomException(HttpStatus.NOT_FOUND, "상대 회원 조회 실패"));

        Friendship friendship = friendshipRepository.findByUserIds(acceptorId, requestUser.getId())
                        .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "신청된 내역이 없습니다."));

        friendship.setValid();
        friendshipRepository.save(friendship);
    }
}
