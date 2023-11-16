package today.seasoning.seasoning.article.service;

import lombok.Getter;

@Getter
public class FindMyArticlesByTermResult {

	private final String id;
	private final int year;
	private final String preview;
	private final String image;

	public FindMyArticlesByTermResult(String id, int year, String preview, String image) {
		this.id = id;
		this.year = year;
		this.preview = preview;
		this.image = image;
	}
}
