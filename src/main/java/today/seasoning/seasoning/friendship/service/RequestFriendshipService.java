package today.seasoning.seasoning.friendship.service;

import java.util.Optional;
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
public class RequestFriendshipService {

	private final UserRepository userRepository;
	private final FriendshipRepository friendshipRepository;

	public void doService(Long fromUserId, String toUserAccountId) {
		User fromUser = userRepository.findById(fromUserId).get();

		User toUser = userRepository.findByAccountId(toUserAccountId)
			.orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "상대 회원 조회 실패"));

		checkAlreadyExists(fromUser, toUser);

		registerFriendships(fromUser, toUser);
	}

	private void checkAlreadyExists(User fromUser, User toUser) {
		Optional<Friendship> friendship = friendshipRepository.findByUserIds(fromUser.getId(),
			toUser.getId());

		if (friendship.isPresent()) {
			throw new CustomException(HttpStatus.CONFLICT, "이미 신청된 내역입니다.");
		}
	}

	private void registerFriendships(User fromUser, User toUser) {
		Friendship forwardFriendship = new Friendship(fromUser, toUser, true);
		Friendship reverseFriendship = new Friendship(toUser, fromUser, false);

		friendshipRepository.save(forwardFriendship);
		friendshipRepository.save(reverseFriendship);
	}

}
