package today.seasoning.seasoning.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        User user = userRepository.findById(id).get();
        return new GetUserProfile(user);
    }

    // 프로필 수정 (닉네임, 이미지 수정 가능)
    public void updateUserProfile(Long id, UpdateUserProfile userProfile) {
        User user = userRepository.findById(id).get();
        user.setNickname(userProfile.getNickname());
        user.setProfileImageUrl(userProfile.getProfileImageUrl());
        userRepository.save(user);
    }
}
