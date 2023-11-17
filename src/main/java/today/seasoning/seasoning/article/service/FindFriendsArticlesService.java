package today.seasoning.seasoning.article.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.article.domain.Article;
import today.seasoning.seasoning.article.domain.ArticleImage;
import today.seasoning.seasoning.article.domain.ArticleRepository;
import today.seasoning.seasoning.article.dto.FindFriendsArticlesResult;
import today.seasoning.seasoning.article.dto.FriendsArticlesDto;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.common.util.TsidUtil;
import today.seasoning.seasoning.friendship.domain.FriendshipRepository;
import today.seasoning.seasoning.user.domain.User;
import today.seasoning.seasoning.user.domain.UserRepository;
import today.seasoning.seasoning.user.dto.GetUserProfile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FindFriendsArticlesService {

    private UserRepository userRepository;
    private FriendshipRepository friendshipRepository;
    private ArticleRepository articleRepository;

    public List<FindFriendsArticlesResult> findFriendsArticles(Long articleId, int size, Long userId) {
        User user = userRepository.findById(userId).get();

        // 사용자 친구들 가져오기
        List<Long> friendsIds = findFriends(userId);

        // 사용자 친구들 글 목록 마지막 게시물보다 이전에 작성한 것 가져오기
        Page<Article> articles = findArticlesByUsers(friendsIds, articleId, size);

        return articles.stream().map(this::toDto).collect(Collectors.toList());
    }

    private FindFriendsArticlesResult toDto(Article article) {
        String contentsPreview = getContentsPreview(article.getContents());

        String thumbnailImageUrl = getFirstImageUrl(article.getArticleImages());

        FriendsArticlesDto friendsArticleDto = new FriendsArticlesDto(
                TsidUtil.toString(article.getId()),
                article.getCreatedYear(),
                article.getCreatedTerm(),
                contentsPreview,
                thumbnailImageUrl);

        User friend = userRepository.findById(article.getUser().getId()).get();
        GetUserProfile friendProfile = new GetUserProfile(friend);

        return new FindFriendsArticlesResult(friendProfile, friendsArticleDto);
    }

    private static String getFirstImageUrl(List<ArticleImage> images) {
        return images
                .stream()
                .min(Comparator.comparingInt(ArticleImage::getSequence))
                .map(ArticleImage::getUrl)
                .orElse(null);
    }

    private static String getContentsPreview(String contents) {
        int previewLength = Math.min(contents.length(), 150);
        return contents.substring(0, previewLength);
    }

    private List<Long> findFriends(Long userId) {
        List<Long> sentFriends = friendshipRepository.findByUserIdIsValidFromUser(userId).orElse(null);
        List<Long> friends = new ArrayList<>();
        for (Long friendId : sentFriends) {
            // 쌍방 친구인 경우
            if (friendshipRepository.findByUserIds(friendId, userId).get().isValid())
                friends.add(friendId);
        }
        return friends;
    }

    private Page<Article> findArticlesByUsers(List<Long> userIds, Long articleId, int size) {
        Article article = articleRepository.findById(articleId).orElseThrow(()->
                new CustomException(HttpStatus.NOT_FOUND, "게시물 조회 실패"));

        PageRequest pageRequest = PageRequest.of(0, size);
        Page<Article> articles = articleRepository.findByUserIdsOlderThanDate(userIds, article.getCreatedDate(), pageRequest);
        return articles;
    }
}
