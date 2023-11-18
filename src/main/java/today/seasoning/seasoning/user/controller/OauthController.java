package today.seasoning.seasoning.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import today.seasoning.seasoning.user.dto.LoginResultDto;
import today.seasoning.seasoning.user.service.KakaoLoginService;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

	private final KakaoLoginService kakaoLoginService;

	@GetMapping("/kakao/login")
	public ResponseEntity<Void> kakaoLogin(@RequestParam("code") String authCode) {

		LoginResultDto loginResult = kakaoLoginService.handleKakaoLogin(authCode);

		return ResponseEntity.ok()
			.header("Authorization", loginResult.getToken())
			.header("Is-First", String.valueOf(loginResult.isNewUser()))
			.build();
	}
}
