package today.seasoning.seasoning.article.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import today.seasoning.seasoning.article.dto.FindArticleResult;
import today.seasoning.seasoning.article.dto.RegisterArticleCommand;
import today.seasoning.seasoning.article.dto.RegisterArticleDto;
import today.seasoning.seasoning.article.dto.UpdateArticleCommand;
import today.seasoning.seasoning.article.dto.UpdateArticleDto;
import today.seasoning.seasoning.article.service.DeleteArticleService;
import today.seasoning.seasoning.article.service.FindArticleService;
import today.seasoning.seasoning.article.service.RegisterArticleService;
import today.seasoning.seasoning.article.service.UpdateArticleService;
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
	public ResponseEntity<Void> deleteArticle(
		@AuthenticationPrincipal UserPrincipal principal,
		@PathVariable String stringArticleId) {

		Long userId = principal.getId();
		Long articleId = TsidUtil.toLong(stringArticleId);

		deleteArticleService.doDelete(userId, articleId);

		return ResponseEntity.ok().build();
	}
}
