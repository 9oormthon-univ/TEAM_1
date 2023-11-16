package today.seasoning.seasoning.article.dto;

import lombok.Getter;

@Getter
public class FindCollageResult {

	private final int term;
	private final String articleId;
	private final String image;

	public FindCollageResult(int term, String articleId, String image) {
		this.term = term;
		this.articleId = articleId;
		this.image = image;
	}
}
