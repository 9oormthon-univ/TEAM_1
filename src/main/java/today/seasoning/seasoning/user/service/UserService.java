package today.seasoning.seasoning.user.service;

import java.util.Optional;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;
import today.seasoning.seasoning.user.dto.GetUserProfile;
import today.seasoning.seasoning.user.dto.UpdateUserProfile;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	// 프로필 조회
	@Transactional(readOnly = true)
	public GetUserProfile findUserProfile(Long userId) {
		User user = userRepository.findById(userId).get();
		return new GetUserProfile(user);
	}

	// 프로필 수정 (닉네임, 이미지 수정 가능)
	@Transactional
	public void updateUserProfile(Long userId, UpdateUserProfile userProfile) {
		User user = userRepository.findById(userId).get();
		user.updateProfile(userProfile.getNickname(), userProfile.getProfileImageUrl());
		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public void validateAccountIdUsability(String accountId) {
		validateAccountIdFormat(accountId);

		Optional<User> registeredAccountId = userRepository.findByAccountId(accountId);
		if (registeredAccountId.isPresent()) {
			throw new CustomException(HttpStatus.CONFLICT, "사용 중인 아이디 입니다.");
		}
	}

	private void validateAccountIdFormat(String accountId) {
		String regex = "^(?!.*\\.\\.)(?!.*\\.$)[^\\W][\\w.]{4,19}$";

		if (accountId.contains(".*[A-Z].*")) {
			throwCustomException("아이디에 대문자는 허용되지 않습니다.");
		}

		if (!Pattern.matches(regex, accountId)) {
			if (accountId.startsWith(".")) {
				throwCustomException("아이디는 마침표로 시작할 수 없습니다.");
			}
			if (accountId.endsWith(".")) {
				throwCustomException("아이디는 마침표로 끝날 수 없습니다.");
			}
			if (accountId.endsWith("..")) {
				throwCustomException("아이디에 연속된 마침표는 허용되지 않습니다.");
			}
			if (accountId.length() < 5) {
				throwCustomException("아이디는 최소 5글자 입니다.");
			}
			if (accountId.length() > 20) {
				throwCustomException("아이디는 최대 20글자 입니다.");
			}
			throwCustomException("사용할 수 없는 아이디 입니다.");
		}
	}

	private void throwCustomException(String errorMessage) {
		throw new CustomException(HttpStatus.BAD_REQUEST, errorMessage);
	}
}
