package pl.gornickifilip.postsfunction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.userId = :userId")
    List<Post> findAllByUserId(UUID userId);
}
