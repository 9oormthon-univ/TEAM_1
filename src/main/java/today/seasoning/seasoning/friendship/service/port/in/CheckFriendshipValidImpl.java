package today.seasoning.seasoning.friendship.service.port.in;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import today.seasoning.seasoning.friendship.domain.Friendship;
import today.seasoning.seasoning.friendship.domain.FriendshipRepository;

@Service
@RequiredArgsConstructor
public class CheckFriendshipValidImpl implements CheckFriendshipValid {

	private final FriendshipRepository friendshipRepository;

	@Override
	public boolean doCheck(Long userId, Long friendId) {
		return hasRequestedFriendshipTo(userId, friendId)
			&& hasRequestedFriendshipTo(friendId, userId);
	}

	public boolean hasRequestedFriendshipTo(Long fromUserId, Long toUserID) {
		return friendshipRepository.findByUserIds(fromUserId, toUserID)
			.map(Friendship::isValid)
			.orElse(false);
	}
}
