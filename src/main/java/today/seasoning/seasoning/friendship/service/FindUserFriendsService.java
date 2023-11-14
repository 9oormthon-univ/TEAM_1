package today.seasoning.seasoning.friendship.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.friendship.domain.FriendshipRepository;
import today.seasoning.seasoning.friendship.domain.FriendshipStatus;
import today.seasoning.seasoning.friendship.dto.FindUserFriendsResult;
import today.seasoning.seasoning.user.domain.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindUserFriendsService {

	private final FriendshipRepository friendshipRepository;

	public List<FindUserFriendsResult> doFind(Long userId) {
		return friendshipRepository.findByToUserId(userId)
			.stream()
			.filter(f -> f.getStatus() == FriendshipStatus.ACCEPTED)
			.map(f -> createFindUserFriendsResult(f.getFromUser()))
			.collect(Collectors.toList());
	}

	private FindUserFriendsResult createFindUserFriendsResult(User friend) {
		return new FindUserFriendsResult(
			friend.getNickname(),
			friend.getAccountId(),
			friend.getProfileImageUrl());
	}
}
