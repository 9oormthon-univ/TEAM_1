package today.seasoning.seasoning.article.dto;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FindArticleResult {

	private final int year;
	private final int term;
	private final String contents;
	private final List<FindArticleImageResult> images;
	private final int like_count;
	private final boolean user_likes;
}
