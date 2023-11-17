package today.seasoning.seasoning.article.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

	@Query("SELECT a From Article a WHERE a.user.id = :userId AND a.createdYear = :year")
	List<Article> findByUserIdAndYear(@Param("userId") Long userId, @Param("year") int year);

	@Query("SELECT a From Article a WHERE a.user.id = :userId AND a.createdTerm = :term")
	List<Article> findByUserIdAndTerm(@Param("userId") Long userId, @Param("term") int term);

	@Query("SELECT a From Article a WHERE a.user.id IN :userIds AND a.createdDate < :olderThanDate " +
			"ORDER BY a.createdDate DESC")
	Page<Article> findByUserIdsOlderThanDate(@Param("userIds") List<Long> userIds,
											 @Param("olderThanDate")LocalDateTime olderThanDate,
											 Pageable pageRequest);
}
