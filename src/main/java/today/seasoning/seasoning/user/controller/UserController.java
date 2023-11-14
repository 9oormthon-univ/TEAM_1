package today.seasoning.seasoning.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import today.seasoning.seasoning.common.UserPrincipal;
import today.seasoning.seasoning.user.dto.GetUserProfile;
import today.seasoning.seasoning.user.dto.UpdateUserProfile;
import today.seasoning.seasoning.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    // 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<GetUserProfile> findUserProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        GetUserProfile userProfile = userService.findUserProfile(userPrincipal.getId());
        return ResponseEntity.ok().body(userProfile);
    }

}
