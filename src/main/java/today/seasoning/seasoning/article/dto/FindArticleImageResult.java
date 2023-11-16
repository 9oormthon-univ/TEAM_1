package today.seasoning.seasoning.article.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import today.seasoning.seasoning.article.domain.ArticleImage;

@Getter
@RequiredArgsConstructor
public class FindArticleImageResult {

	private final int sequence;
	private final String url;

	public static FindArticleImageResult build(ArticleImage articleImage) {
		return new FindArticleImageResult(
			articleImage.getSequence(),
			articleImage.getUrl());
	}
}
