package today.seasoning.seasoning.article.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import today.seasoning.seasoning.article.dto.RegisterArticleCommand;
import today.seasoning.seasoning.article.dto.RegisterArticleWebRequest;
import today.seasoning.seasoning.article.service.RegisterArticleService;
import today.seasoning.seasoning.common.UserPrincipal;
import today.seasoning.seasoning.common.util.TsidUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {

	private final RegisterArticleService registerArticleService;

	@PostMapping
	public ResponseEntity<String> registerArticle(@AuthenticationPrincipal UserPrincipal principal,
		@RequestBody @Valid RegisterArticleWebRequest webRequest) {

		RegisterArticleCommand command = new RegisterArticleCommand(principal.getId(),
			webRequest.getIsPublic(),
			webRequest.getContents(),
			webRequest.getImages());

		Long registeredArticleId = registerArticleService.doRegister(command);

		return ResponseEntity.ok(TsidUtil.toString(registeredArticleId));
	}
}
