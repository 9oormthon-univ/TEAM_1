package today.seasoning.seasoning.article.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class RegisterArticleWebRequest {

	@NotNull
	private Boolean isPublic;

	@NotNull
	private String contents;

	@Valid
	@NotNull
	private List<ArticleImageDto> images;
}
