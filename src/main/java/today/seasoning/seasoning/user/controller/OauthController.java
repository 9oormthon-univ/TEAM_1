package today.seasoning.seasoning.user.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import today.seasoning.seasoning.user.dto.LoginResultDto;
import today.seasoning.seasoning.user.service.KakaoLoginService;

@RestController
@RequiredArgsConstructor
public class OauthController {

	private final KakaoLoginService kakaoLoginService;

	@Value("${KAKAO_LOGIN_URL}")
	private String kakaoLoginUrl;

	@Value("${AFTER_LOGIN_REDIRECT_URL}")
	private String afterLoginRedirectUrl;

	@GetMapping("/kakao/login")
	public void redirectToKakaoLogin(HttpServletResponse response) throws IOException {
		response.sendRedirect(kakaoLoginUrl);
	}

	@GetMapping("/oauth/kakao/login")
	public void kakaoLogin(@RequestParam("code") String authCode, HttpServletResponse response)
		throws IOException {

		LoginResultDto loginResult = kakaoLoginService.handleKakaoLogin(authCode);
		boolean isNewUser = loginResult.isNewUser();

		response.addHeader("Authorization", loginResult.getToken());
		response.getWriter().print(loginResult.isNewUser());
		response.sendRedirect(afterLoginRedirectUrl + "?new=" + isNewUser);
	}
}
