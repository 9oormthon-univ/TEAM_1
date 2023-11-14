package today.seasoning.seasoning.friendship.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

	@Query("SELECT f FROM Friendship f WHERE f.fromUser.id = :fromUserId AND f.toUser.id = :toUserId")
	Optional<Friendship> findByUserIds(
		@Param("fromUserId") Long fromUserId,
		@Param("toUserId") Long toUserId);
}
