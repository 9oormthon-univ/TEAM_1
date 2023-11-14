package today.seasoning.seasoning.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UpdateUserProfile {

    private final String nickname;
    private final String profileImageUrl;
}
