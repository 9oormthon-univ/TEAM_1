package today.seasoning.seasoning.article.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import today.seasoning.seasoning.article.domain.ArticleImage;
import today.seasoning.seasoning.common.util.TsidUtil;

@Getter
@RequiredArgsConstructor
public class FindArticleImageResult {

	private final String id;
	private final int sequence;
	private final String url;

	public static FindArticleImageResult build(ArticleImage articleImage) {
		return new FindArticleImageResult(
			TsidUtil.toString(articleImage.getId()),
			articleImage.getSequence(),
			articleImage.getUrl());
	}
}
