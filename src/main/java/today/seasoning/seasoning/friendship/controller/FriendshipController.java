package today.seasoning.seasoning.friendship.controller;

import java.util.List;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import today.seasoning.seasoning.common.UserPrincipal;
import today.seasoning.seasoning.friendship.dto.FindUserFriendsResult;
import today.seasoning.seasoning.friendship.dto.SearchFriendResult;
import today.seasoning.seasoning.friendship.dto.AccountIdDto;
import today.seasoning.seasoning.friendship.service.*;

@RequestMapping("/friend")
@RestController
@RequiredArgsConstructor
public class FriendshipController {

    private final RequestFriendshipService requestFriendshipService;
    private final FindUserFriendsService findUserFriendsService;
    private final AcceptFriendshipService acceptFriendshipService;
    private final CancelFriendshipService cancelFriendshipService;
    private final DeclineFriendshipService declineFriendshipService;
    private final DeleteFriendshipService deleteFriendshipService;
    private final SearchFriendshipService searchFriendshipService;

    @RequestMapping("/add")
    public ResponseEntity<String> requestFriendship(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AccountIdDto accountIdDto) {

        Long fromUserId = principal.getId();
        String toUserAccountId = accountIdDto.getAccountId();

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

    @PutMapping("/add/accept")
    public ResponseEntity<String> acceptFriendship(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AccountIdDto accountIdDto) {

        Long userId = principal.getId();
        String requesterAccountId = accountIdDto.getAccountId();

        acceptFriendshipService.doService(userId, requesterAccountId);

        return ResponseEntity.ok().body("수락 완료");
    }

    @DeleteMapping("/add/cancel")
    public ResponseEntity<String> cancelFriendship(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AccountIdDto accountIdDto) {

        Long userId = principal.getId();
        String toUserAccountId = accountIdDto.getAccountId();

        cancelFriendshipService.doService(userId, toUserAccountId);

        return ResponseEntity.ok().body("취소 완료");
    }

    @DeleteMapping("/add/decline")
    public ResponseEntity<String> declineFriendship(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AccountIdDto accountIdDto) {

        Long userId = principal.getId();
        String toUserAccountId = accountIdDto.getAccountId();

        declineFriendshipService.doService(userId, toUserAccountId);

        return ResponseEntity.ok().body("거절 완료");
    }

    @DeleteMapping("/unfriend")
    public ResponseEntity<String> deleteFriendship(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AccountIdDto accountIdDto) {

        Long userId = principal.getId();
        String toUserAccountId = accountIdDto.getAccountId();

        deleteFriendshipService.doService(userId, toUserAccountId);

        return ResponseEntity.ok().body("삭제 완료");
    }

    @GetMapping("/search")
    public ResponseEntity<SearchFriendResult> searchFriend(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam("keyword") String friendAccountId) {

        Long userId = principal.getId();

        SearchFriendResult result = searchFriendshipService.doService(userId, friendAccountId);

        return ResponseEntity.ok().body(result);
    }
}
