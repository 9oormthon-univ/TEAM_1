package today.seasoning.seasoning.friendship.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import today.seasoning.seasoning.common.UserPrincipal;
import today.seasoning.seasoning.friendship.dto.FindUserFriendsResult;
import today.seasoning.seasoning.friendship.dto.ToUserAccountIdDto;
import today.seasoning.seasoning.friendship.service.FindUserFriendsService;
import today.seasoning.seasoning.friendship.service.RequestFriendshipService;

@RequestMapping("/friend")
@RestController
@RequiredArgsConstructor
public class FriendshipController {

	private final RequestFriendshipService requestFriendshipService;
	private final FindUserFriendsService findUserFriendsService;

	@RequestMapping("/add")
	public ResponseEntity<String> requestFriendship(
		@AuthenticationPrincipal UserPrincipal principal,
		@Valid @RequestBody ToUserAccountIdDto toUserAccountIdDto) {

		Long fromUserId = principal.getId();
		String toUserAccountId = toUserAccountIdDto.getAccountId();

		requestFriendshipService.doService(fromUserId, toUserAccountId);

		return ResponseEntity.ok().body("신청 완료");
	}

	@GetMapping("/list")
	public ResponseEntity<List<FindUserFriendsResult>> findUserFriends(
		@AuthenticationPrincipal UserPrincipal principal) {

		Long userId = principal.getId();

		List<FindUserFriendsResult> findUserFriendResults = findUserFriendsService.doFind(userId);

		return ResponseEntity.ok().body(findUserFriendResults);
	}
}
