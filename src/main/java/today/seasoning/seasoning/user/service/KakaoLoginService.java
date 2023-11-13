package today.seasoning.seasoning.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.common.enums.LoginType;
import today.seasoning.seasoning.common.util.JwtUtil;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;
import today.seasoning.seasoning.user.dto.UserProfile;
import today.seasoning.seasoning.user.service.port.kakao.ExchangeKakaoAccessToken;
import today.seasoning.seasoning.user.service.port.kakao.FetchKakaoUserProfile;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLoginService {

	private static final LoginType KAKAO_LOGIN_TYPE = LoginType.KAKAO;

	private final ExchangeKakaoAccessToken exchangeKakaoAccessToken;
	private final FetchKakaoUserProfile fetchKakaoUserProfile;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Transactional
	public String handleKakaoLogin(String authorizationCode) {
		String accessToken = exchangeKakaoAccessToken.doExchange(authorizationCode);

		UserProfile userProfile = fetchKakaoUserProfile.doFetch(accessToken);

		User user = userRepository.find(userProfile.getEmail(), KAKAO_LOGIN_TYPE)
			.orElseGet(() -> registerUser(userProfile));

		return jwtUtil.createToken(user.getId(), KAKAO_LOGIN_TYPE);
	}

	private User registerUser(UserProfile userProfile) {
		User user = new User(
			userProfile.getNickname(),
			userProfile.getProfileImageUrl(),
			userProfile.getEmail(),
			KAKAO_LOGIN_TYPE);

		return userRepository.save(user);
	}
}
