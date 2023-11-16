package today.seasoning.seasoning.article.dto;

import lombok.Getter;

@Getter
public class FindMyArticlesByYearResult {

	private final String id;
	private final int term;

	public FindMyArticlesByYearResult(String id, int term) {
		this.id = id;
		this.term = term;
	}
}
