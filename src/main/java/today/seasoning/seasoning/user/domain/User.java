package today.seasoning.seasoning.user.domain;

import com.github.f4b6a3.tsid.TsidCreator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import today.seasoning.seasoning.common.BaseTimeEntity;
import today.seasoning.seasoning.common.enums.LoginType;

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
}
