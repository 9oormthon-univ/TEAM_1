package today.seasoning.seasoning.fortune.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FortuneRepository extends JpaRepository<Fortune, Long> {
    @Query("SELECT f.content FROM Fortune f ORDER BY RAND() LIMIT 1")
    Optional<String> findByRandom();
}
