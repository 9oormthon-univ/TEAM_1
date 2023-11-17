package today.seasoning.seasoning.article.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import today.seasoning.seasoning.article.dto.*;
import today.seasoning.seasoning.article.service.*;
import today.seasoning.seasoning.common.UserPrincipal;
import today.seasoning.seasoning.common.util.TsidUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

	private final RegisterArticleService registerArticleService;
	private final FindArticleService findArticleService;
	private final UpdateArticleService updateArticleService;
	private final DeleteArticleService deleteArticleService;
	private final FindMyArticlesByYearService findMyArticlesByYearService;
	private final FindMyArticlesByTermService findMyArticlesByTermService;
	private final ArticleLikeService articleLikeService;
	private final FindCollageService findCollageService;
	private final FindFriendsArticlesService findFriendsArticlesService;

	@PostMapping
	public ResponseEntity<String> registerArticle(@AuthenticationPrincipal UserPrincipal principal,
		@RequestPart(name = "images", required = false) List<MultipartFile> images,
		@RequestPart("request") @Valid RegisterArticleDto registerArticleDto) {

		RegisterArticleCommand command = new RegisterArticleCommand(principal.getId(),
			registerArticleDto.getPublished(),
			registerArticleDto.getContents(),
			images);

		Long articleId = registerArticleService.doRegister(command);

		String stringArticleId = TsidUtil.toString(articleId);
		return ResponseEntity.ok(stringArticleId);
	}

	@GetMapping("/{stringArticleId}")
	public ResponseEntity<FindArticleResult> findArticle(
		@AuthenticationPrincipal UserPrincipal principal,
		@PathVariable String stringArticleId) {

		Long userId = principal.getId();
		Long articleId = TsidUtil.toLong(stringArticleId);

		FindArticleResult findArticleResult = findArticleService.doFind(userId, articleId);

		return ResponseEntity.ok(findArticleResult);
	}

	@PutMapping("/{stringArticleId}")
	public ResponseEntity<Void> updateArticle(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestPart(name = "images", required = false) List<MultipartFile> images,
		@RequestPart("request") @Valid UpdateArticleDto updateArticleDto,
		@PathVariable String stringArticleId) {

		UpdateArticleCommand command = new UpdateArticleCommand(userPrincipal.getId(),
			TsidUtil.toLong(stringArticleId),
			updateArticleDto.getPublished(),
			updateArticleDto.getContents(),
			images);

		updateArticleService.doUpdate(command);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{stringArticleId}")
	public ResponseEntity<Void> deleteArticle(@AuthenticationPrincipal UserPrincipal principal,
		@PathVariable String stringArticleId) {

		Long userId = principal.getId();
		Long articleId = TsidUtil.toLong(stringArticleId);

		deleteArticleService.doDelete(userId, articleId);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/list/year/{year}")
	public ResponseEntity<List<FindMyArticlesByYearResult>> findMyArticlesByYear(
		@AuthenticationPrincipal UserPrincipal principal,
		@PathVariable Integer year) {

		Long userId = principal.getId();

		List<FindMyArticlesByYearResult> result = findMyArticlesByYearService.doFind(userId, year);

		return ResponseEntity.ok(result);
	}

	@GetMapping("/list/term/{term}")
	public ResponseEntity<List<FindMyArticlesByTermResult>> findMyArticlesByTerm(
		@AuthenticationPrincipal UserPrincipal principal,
		@PathVariable Integer term) {

		Long userId = principal.getId();

		List<FindMyArticlesByTermResult> result = findMyArticlesByTermService.doFind(userId, term);

		return ResponseEntity.ok(result);
	}

	@PostMapping("{articleId}/like")
	public ResponseEntity<Void> likeArticle(@AuthenticationPrincipal UserPrincipal principal,
		@PathVariable("articleId") String stringArticleId) {

		Long userId = principal.getId();
		Long articleId = TsidUtil.toLong(stringArticleId);

		articleLikeService.doLike(userId, articleId);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("{articleId}/like")
	public ResponseEntity<Void> cancelLikeArticle(@AuthenticationPrincipal UserPrincipal principal,
		@PathVariable("articleId") String stringArticleId) {

		Long userId = principal.getId();
		Long articleId = TsidUtil.toLong(stringArticleId);

		articleLikeService.cancelLike(userId, articleId);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/collage")
	public ResponseEntity<List<FindCollageResult>> findCollage(
		@AuthenticationPrincipal UserPrincipal principal,
		@RequestParam("year") Integer year) {

		Long userId = principal.getId();
		List<FindCollageResult> collage = findCollageService.doFind(userId, year);
		return ResponseEntity.ok(collage);
	}

	@GetMapping("/friends")
	public ResponseEntity<List<FindFriendsArticlesResult>> findFriendsArticles(
			@AuthenticationPrincipal UserPrincipal principal,
			@RequestBody FindFriendsArticleResponse response) {

		Long userId = principal.getId();
		Long articleId = TsidUtil.toLong(response.getArticleId());
		int size = response.getSize();

		List<FindFriendsArticlesResult> result = findFriendsArticlesService.findFriendsArticles(articleId, size, userId);
		return ResponseEntity.ok().body(result);
	}
}
