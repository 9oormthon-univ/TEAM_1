package today.seasoning.seasoning.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import today.seasoning.seasoning.user.service.KakaoLoginService;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

	private final KakaoLoginService kakaoLoginService;

	@GetMapping("/kakao/login")
	public ResponseEntity<Void> kakaoLogin(@RequestParam("code") String authCode) {

		String token = kakaoLoginService.handleKakaoLogin(authCode);

		return ResponseEntity.ok()
			.header("Authorization", token)
			.build();
	}
}
