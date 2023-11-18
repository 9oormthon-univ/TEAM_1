package today.seasoning.seasoning.friendship.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountIdDto {

	@NotBlank
	private String accountId;
}
