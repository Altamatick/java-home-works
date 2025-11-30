package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("h2")
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSavePost() {
        User user = userRepository.save(new User("John Doe", "john@example.com"));
        Post post = new Post("Test Title", "Test Content");
        post.setUser(user);

        Post saved = postRepository.save(post);

        assertNotNull(saved.getId());
        assertEquals("Test Title", saved.getTitle());
        assertEquals("Test Content", saved.getContent());
        assertEquals(user.getId(), saved.getUser().getId());
    }

    @Test
    void testFindByUserId() {
        User user1 = userRepository.save(new User("User1", "user1@example.com"));
        User user2 = userRepository.save(new User("User2", "user2@example.com"));

        Post post1 = new Post("Post 1", "Content 1");
        post1.setUser(user1);
        postRepository.save(post1);

        Post post2 = new Post("Post 2", "Content 2");
        post2.setUser(user1);
        postRepository.save(post2);

        Post post3 = new Post("Post 3", "Content 3");
        post3.setUser(user2);
        postRepository.save(post3);

        List<Post> user1Posts = postRepository.findByUserId(user1.getId());

        assertEquals(2, user1Posts.size());
        assertTrue(user1Posts.stream().allMatch(p -> p.getUser().getId().equals(user1.getId())));
    }

    @Test
    void testFindByUserIdNoPosts() {
        User user = userRepository.save(new User("User1", "user1@example.com"));

        List<Post> posts = postRepository.findByUserId(user.getId());

        assertTrue(posts.isEmpty());
    }

    @Test
    void testFindByUserIdNonExistent() {
        List<Post> posts = postRepository.findByUserId(999L);

        assertTrue(posts.isEmpty());
    }

    @Test
    void testDeletePost() {
        User user = userRepository.save(new User("John Doe", "john@example.com"));
        Post post = new Post("Title", "Content");
        post.setUser(user);
        Post saved = postRepository.save(post);

        postRepository.deleteById(saved.getId());

        assertFalse(postRepository.findById(saved.getId()).isPresent());
    }
}
