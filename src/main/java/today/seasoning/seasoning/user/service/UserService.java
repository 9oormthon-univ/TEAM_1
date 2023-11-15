package today.seasoning.seasoning.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
}
