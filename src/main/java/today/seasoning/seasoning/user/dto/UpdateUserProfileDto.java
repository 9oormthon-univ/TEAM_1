package today.seasoning.seasoning.user.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserProfileDto {

	@NotBlank
	private String accountId;

	@NotBlank
	private String nickname;
}
