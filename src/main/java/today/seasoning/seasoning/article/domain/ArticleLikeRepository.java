package today.seasoning.seasoning.article.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

	@Query("SELECT a FROM ArticleLike a WHERE a.article.id = :articleId AND a.user.id = :userId")
	Optional<ArticleLike> findByArticleAndUser(
		@Param("articleId") Long articleId,
		@Param("userId") Long userId);
}
