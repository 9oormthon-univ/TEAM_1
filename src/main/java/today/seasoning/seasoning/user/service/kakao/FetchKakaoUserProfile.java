package today.seasoning.seasoning.user.service.kakao;

import today.seasoning.seasoning.user.dto.SocialUserProfileDto;

public interface FetchKakaoUserProfile {

	SocialUserProfileDto doFetch(String accessToken);
}
