package today.seasoning.seasoning.article.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import today.seasoning.seasoning.user.dto.GetUserProfile;

@Getter
@RequiredArgsConstructor
public class FindFriendsArticlesResult {
    private final GetUserProfile friendProfile;
    private final FriendsArticlesDto friendArticle;
    // 무한 스크롤이라 page 관련 변수는 필요 없을 듯
}
