package today.seasoning.seasoning.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;
import today.seasoning.seasoning.user.dto.UserProfileDto;

@Service
@RequiredArgsConstructor
public class FindUserProfileService {

	private final UserRepository userRepository;

	// 프로필 조회
	@Transactional(readOnly = true)
	public UserProfileDto findUserProfile(Long userId) {
		User user = userRepository.findById(userId).get();
		return UserProfileDto.build(user);
	}
}
