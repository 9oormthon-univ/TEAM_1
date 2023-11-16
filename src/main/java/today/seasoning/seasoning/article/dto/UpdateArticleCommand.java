package today.seasoning.seasoning.article.dto;

import java.util.List;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UpdateArticleCommand {

	private final Long userId;
	private final Long articleId;
	private final boolean isPublic;
	private final String contents;
	private final List<MultipartFile> images;

	public UpdateArticleCommand(Long userId, Long articleId, boolean isPublic, String contents,
		List<MultipartFile> images) {
		this.userId = userId;
		this.articleId = articleId;
		this.isPublic = isPublic;
		this.contents = contents;
		this.images = images;
	}
}
