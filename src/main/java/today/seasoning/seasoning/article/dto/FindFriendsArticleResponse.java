package today.seasoning.seasoning.article.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FindFriendsArticleResponse {
    private String articleId;
    private int size;
}
