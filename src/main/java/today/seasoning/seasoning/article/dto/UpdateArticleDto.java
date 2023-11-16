package today.seasoning.seasoning.article.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateArticleDto {

	@NotNull
	private Boolean published;

	@NotNull
	private String contents;
}
