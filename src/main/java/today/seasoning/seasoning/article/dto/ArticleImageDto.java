package today.seasoning.seasoning.article.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleImageDto {

	@NotNull
	@Min(value = 1)
	private Integer sequence;

	@NotBlank
	private String url;
}
