package today.seasoning.seasoning.user.domain;

import com.github.f4b6a3.tsid.TsidCreator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import today.seasoning.seasoning.common.BaseTimeEntity;
import today.seasoning.seasoning.common.enums.LoginType;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uix-email-login_type", columnNames = {"email", "login_type"})})
public class User extends BaseTimeEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String nickname;

    private String profileImageUrl;

    @Column(unique = true, nullable = false)
    private String accountId;

    @Column(nullable = false)
    private String email;

    @Column(name = "login_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    public User(String nickname, String profileImageUrl, String email, LoginType loginType) {
        this.id = TsidCreator.getTsid().toLong();
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.accountId = TsidCreator.getTsid().toString(); // 최초 랜덤값
        this.email = email;
        this.loginType = loginType;
    }

    public void updateProfile(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
