package today.seasoning.seasoning.article.dto;

import lombok.Getter;

@Getter
public class FindCollageResult {

	private final String articleId;
	private final String image;
	private final int term;

	public FindCollageResult(String articleId, String image, int term) {
		this.articleId = articleId;
		this.image = image;
		this.term = term;
	}
}
