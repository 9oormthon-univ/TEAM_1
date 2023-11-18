package today.seasoning.seasoning.user.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.common.enums.LoginType;
import today.seasoning.seasoning.common.util.JwtUtil;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;
import today.seasoning.seasoning.user.dto.LoginResultDto;
import today.seasoning.seasoning.user.dto.SocialUserProfileDto;
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
	public LoginResultDto handleKakaoLogin(String authorizationCode) {
		String accessToken = exchangeKakaoAccessToken(authorizationCode);
		SocialUserProfileDto userProfile = fetchKakaoUserProfile(accessToken);

		Optional<User> foundUser = userRepository.find(userProfile.getEmail(), KAKAO_LOGIN_TYPE);
		boolean isNewUser = foundUser.isEmpty();

		User user = foundUser.orElseGet(() -> registerUser(userProfile));

		String token = createToken(user);

		return new LoginResultDto(token, isNewUser);
	}

	private String exchangeKakaoAccessToken(String authorizationCode) {
		return exchangeKakaoAccessToken.doExchange(authorizationCode);
	}

	private SocialUserProfileDto fetchKakaoUserProfile(String accessToken) {
		return fetchKakaoUserProfile.doFetch(accessToken);
	}

	private String createToken(User user) {
		return jwtUtil.createToken(user.getId(), KAKAO_LOGIN_TYPE);
	}

	private User registerUser(SocialUserProfileDto userProfile) {
		User user = new User(
			userProfile.getNickname(),
			userProfile.getProfileImageUrl(),
			userProfile.getEmail(),
			KAKAO_LOGIN_TYPE);

		return userRepository.save(user);
	}
}
