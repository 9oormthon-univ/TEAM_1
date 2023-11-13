package today.seasoning.seasoning.common;

import java.io.Serializable;
import lombok.Getter;
import today.seasoning.seasoning.common.enums.LoginType;

@Getter
public class UserPrincipal implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;
    private final LoginType loginType;

    public UserPrincipal(Long id, LoginType loginType) {
        this.id = id;
        this.loginType = loginType;
    }
}
