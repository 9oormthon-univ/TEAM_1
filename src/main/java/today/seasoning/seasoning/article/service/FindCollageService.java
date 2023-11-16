package today.seasoning.seasoning.article.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.article.domain.Article;
import today.seasoning.seasoning.article.domain.ArticleImage;
import today.seasoning.seasoning.article.domain.ArticleRepository;
import today.seasoning.seasoning.article.dto.FindCollageResult;
import today.seasoning.seasoning.common.exception.CustomException;
import today.seasoning.seasoning.common.util.TsidUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindCollageService {

	private final ArticleRepository articleRepository;

	public List<FindCollageResult> doFind(Long userId, int year) {
		validateYear(year);

		return articleRepository.findByUserIdAndYear(userId, year)
			.stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	private void validateYear(Integer year) {
		if (year == null || year < 2023 || year > 2100) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "조회 년도 오류");
		}
	}

	public FindCollageResult toDto(Article article) {
		List<ArticleImage> articleImages = article.getArticleImages();

		String firstImageUrl = getFirstImageUrl(articleImages);

		return new FindCollageResult(
			TsidUtil.toString(article.getId()),
			firstImageUrl,
			article.getCreatedTerm());
	}

	private String getFirstImageUrl(List<ArticleImage> images) {
		return images
			.stream()
			.min(Comparator.comparingInt(ArticleImage::getSequence))
			.map(ArticleImage::getUrl)
			.orElse(null);
	}
}
