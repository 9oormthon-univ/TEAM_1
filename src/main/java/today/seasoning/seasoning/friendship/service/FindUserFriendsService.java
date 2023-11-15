package today.seasoning.seasoning.friendship.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.friendship.domain.Friendship;
import today.seasoning.seasoning.friendship.domain.FriendshipRepository;
import today.seasoning.seasoning.friendship.dto.FindUserFriendsResult;
import today.seasoning.seasoning.user.domain.User;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindUserFriendsService {

	private final FriendshipRepository friendshipRepository;

	public List<FindUserFriendsResult> doFind(Long userId) {
		return friendshipRepository.findByToUserId(userId)
			.stream()
			.map(Friendship::getFromUser)
			.filter(candidate -> checkUserAccepted(userId, candidate))
			.map(this::createFindUserFriendsResult)
			.collect(Collectors.toList());
	}

	private boolean checkUserAccepted(Long userId, User candidate) {
		Long candidateId = candidate.getId();

		return friendshipRepository.findByUserIds(userId, candidateId)
			.map(Friendship::isValid)
			.orElseGet(() -> {
				log.error("Friendship Not Found: Friendship {} -> {}", userId, candidateId);
				return false;
			});
	}

	private FindUserFriendsResult createFindUserFriendsResult(User friend) {
		return new FindUserFriendsResult(
			friend.getNickname(),
			friend.getAccountId(),
			friend.getProfileImageUrl());
	}
}
