package today.seasoning.seasoning.article.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.seasoning.seasoning.article.domain.Article;
import today.seasoning.seasoning.article.domain.ArticleRepository;
import today.seasoning.seasoning.article.dto.FindMyArticlesByYearResult;
import today.seasoning.seasoning.common.util.TsidUtil;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindMyArticlesByYearService {

	private final ArticleRepository articleRepository;

	public List<FindMyArticlesByYearResult> doFind(Long userId, int year) {
		return articleRepository.findByUserIdAndYear(userId, year)
			.stream()
			.map(this::toDto)
			.collect(Collectors.toList());
	}

	private FindMyArticlesByYearResult toDto(Article article) {
		return new FindMyArticlesByYearResult(
			TsidUtil.toString(article.getId()),
			article.getCreatedTerm());
	}
}
