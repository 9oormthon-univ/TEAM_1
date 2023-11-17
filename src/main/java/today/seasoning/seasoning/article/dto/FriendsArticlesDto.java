package today.seasoning.seasoning.article.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FriendsArticlesDto {
    private final String id;
    private final int year;
    private final int term;
    private final String preview;
    private final String image;
}
