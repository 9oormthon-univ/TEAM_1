package today.seasoning.seasoning.article.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class RegisterArticleDto {

	@NotNull
	private Boolean isPublic;

	@NotNull
	private String contents;
}
