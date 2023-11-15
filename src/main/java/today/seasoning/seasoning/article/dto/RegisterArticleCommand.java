package today.seasoning.seasoning.article.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class RegisterArticleCommand {

	private final Long userId;
	private final boolean isPublic;
	private final String contents;
	private final List<ArticleImageDto> articleImages;

	public RegisterArticleCommand(Long userId, boolean isPublic, String contents,
		List<ArticleImageDto> articleImages) {
		this.userId = userId;
		this.isPublic = isPublic;
		this.contents = contents;
		this.articleImages = articleImages;
	}
}
