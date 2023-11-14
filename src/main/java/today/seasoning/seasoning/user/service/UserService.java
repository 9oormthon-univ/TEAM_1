package today.seasoning.seasoning.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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
    public GetUserProfile findUserProfile(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "사용자 정보 조회 실패"));
        return new GetUserProfile(user);
    }

}
