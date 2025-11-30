package app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository для роботи з постами.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Знаходить всі пости користувача за його ідентифікатором
     */
    List<Post> findByUserId(Long userId);
}
