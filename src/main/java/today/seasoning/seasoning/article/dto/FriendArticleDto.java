package today.seasoning.seasoning.article.dto;

import lombok.Getter;

@Getter
public class FriendArticleDto {

	private final String id;
	private final int year;
	private final int term;
	private final String preview;
	private final String image;

	public FriendArticleDto(String id, int year, int term, String preview, String image) {
		this.id = id;
		this.year = year;
		this.term = term;
		this.preview = preview;
		this.image = image;
	}
}
