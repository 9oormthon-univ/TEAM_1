package today.seasoning.seasoning.user.service;

import java.util.Optional;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;

@Service
@RequiredArgsConstructor
public class ValidateAccountIdUsabilityImpl implements ValidateAccountIdUsability {

	private final UserRepository userRepository;

	@Override
	public void doValidate(String accountId) {
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
