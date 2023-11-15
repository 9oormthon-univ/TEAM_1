package today.seasoning.seasoning.article.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import today.seasoning.seasoning.article.dto.FindArticleResult;
import today.seasoning.seasoning.article.dto.RegisterArticleCommand;
import today.seasoning.seasoning.article.dto.RegisterArticleWebRequest;
import today.seasoning.seasoning.article.service.FindArticleService;
import today.seasoning.seasoning.article.service.RegisterArticleService;
import today.seasoning.seasoning.common.UserPrincipal;
import today.seasoning.seasoning.common.util.TsidUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

	private final RegisterArticleService registerArticleService;
	private final FindArticleService findArticleService;

	@PostMapping
	public ResponseEntity<String> registerArticle(@AuthenticationPrincipal UserPrincipal principal,
		@RequestBody @Valid RegisterArticleWebRequest webRequest) {

		RegisterArticleCommand command = new RegisterArticleCommand(principal.getId(),
			webRequest.getIsPublic(),
			webRequest.getContents(),
			webRequest.getImages());

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
}
