package today.seasoning.seasoning.user.service.port.kakao;

import today.seasoning.seasoning.user.dto.UserProfile;

public interface FetchKakaoUserProfile {

	UserProfile doFetch(String accessToken);
}
