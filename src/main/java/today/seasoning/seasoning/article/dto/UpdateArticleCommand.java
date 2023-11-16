package today.seasoning.seasoning.article.dto;

import java.util.List;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UpdateArticleCommand {

	private final Long userId;
	private final Long articleId;
	private final boolean published;
	private final String contents;
	private final List<MultipartFile> images;

	public UpdateArticleCommand(Long userId, Long articleId, boolean published, String contents,
		List<MultipartFile> images) {
		this.userId = userId;
		this.articleId = articleId;
		this.published = published;
		this.contents = contents;
		this.images = images;
	}
}
