package today.seasoning.seasoning.user.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import today.seasoning.seasoning.common.UserPrincipal;
import today.seasoning.seasoning.user.dto.UpdateUserProfileCommand;
import today.seasoning.seasoning.user.dto.UpdateUserProfileDto;
import today.seasoning.seasoning.user.dto.UserProfileDto;
import today.seasoning.seasoning.user.service.FindUserProfileService;
import today.seasoning.seasoning.user.service.UpdateUserProfileService;
import today.seasoning.seasoning.user.service.ValidateAccountIdUsability;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final FindUserProfileService findUserProfileService;
	private final ValidateAccountIdUsability validateAccountIdUsability;
	private final UpdateUserProfileService updateUserProfile;

	// 프로필 조회
	@GetMapping("/profile")
	public ResponseEntity<UserProfileDto> findUserProfile(
		@AuthenticationPrincipal UserPrincipal userPrincipal) {

		UserProfileDto userProfile = findUserProfileService.findUserProfile(userPrincipal.getId());
		return ResponseEntity.ok().body(userProfile);
	}

	// 프로필 수정
	@PutMapping("/profile")
	public ResponseEntity<Void> updateUserProfile(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestPart(name = "image", required = false) MultipartFile profileImage,
		@RequestPart(name = "request") @Valid UpdateUserProfileDto dto) {

		UpdateUserProfileCommand command = new UpdateUserProfileCommand(
			userPrincipal.getId(),
			dto.getAccountId(),
			dto.getNickname(),
			profileImage);

		updateUserProfile.doUpdate(command);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/check-account-id")
	public ResponseEntity<Void> checkAccountId(@RequestParam("id") String accountId) {
		validateAccountIdUsability.doValidate(accountId);
		return ResponseEntity.ok().build();
	}
}
