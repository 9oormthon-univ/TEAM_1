package today.seasoning.seasoning.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;
import today.seasoning.seasoning.user.dto.GetUserProfile;

@Service
@RequiredArgsConstructor
public class FindUserProfileService {

	private final UserRepository userRepository;

	// 프로필 조회
	@Transactional(readOnly = true)
	public GetUserProfile findUserProfile(Long userId) {
		User user = userRepository.findById(userId).get();
		return new GetUserProfile(user);
	}
}
