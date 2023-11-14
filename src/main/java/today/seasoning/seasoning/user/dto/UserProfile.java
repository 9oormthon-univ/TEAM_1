package today.seasoning.seasoning.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import today.seasoning.seasoning.user.domain.User;

@Getter
@Setter
@RequiredArgsConstructor
public class UserProfile {

    private final String nickname;
    private final String email;
    private final String profileImageUrl;

}
