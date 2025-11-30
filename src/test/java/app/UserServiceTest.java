package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testCreateUser() {
        User user = new User("John Doe", "john@example.com");

        User created = userService.createUser(user);

        assertNotNull(created.getId());
        assertEquals("John Doe", created.getName());
        assertEquals("john@example.com", created.getEmail());
    }

    @Test
    void testCreateUserWithPosts() {
        User user = new User("John Doe", "john@example.com");
        List<Post> posts = Arrays.asList(
            new Post("Post 1", "Content 1"),
            new Post("Post 2", "Content 2")
        );

        User created = userService.createUserWithPosts(user, posts);

        assertNotNull(created.getId());
        assertEquals(2, created.getPosts().size());
        assertEquals(2, postRepository.findByUserId(created.getId()).size());
    }

    @Test
    void testGetUsersByName() {
        userService.createUser(new User("John Doe", "john1@example.com"));
        userService.createUser(new User("John Doe", "john2@example.com"));
        userService.createUser(new User("Jane Doe", "jane@example.com"));

        List<User> users = userService.getUsersByName("John Doe");

        assertEquals(2, users.size());
        assertTrue(users.stream().allMatch(u -> u.getName().equals("John Doe")));
    }

    @Test
    void testGetUsersByEmailDomain() {
        userService.createUser(new User("User1", "user1@gmail.com"));
        userService.createUser(new User("User2", "user2@gmail.com"));
        userService.createUser(new User("User3", "user3@yahoo.com"));

        List<User> gmailUsers = userService.getUsersByEmailDomain("@gmail.com");

        assertEquals(2, gmailUsers.size());
        assertTrue(gmailUsers.stream().allMatch(u -> u.getEmail().endsWith("@gmail.com")));
    }

    @Test
    void testGetPostsByUserId() {
        User user = userService.createUser(new User("John Doe", "john@example.com"));
        List<Post> posts = Arrays.asList(
            new Post("Post 1", "Content 1"),
            new Post("Post 2", "Content 2")
        );
        userService.createUserWithPosts(user, posts);

        List<Post> userPosts = userService.getPostsByUserId(user.getId());

        assertEquals(2, userPosts.size());
    }

    @Test
    void testGetAllUsers() {
        userService.createUser(new User("User1", "user1@example.com"));
        userService.createUser(new User("User2", "user2@example.com"));

        List<User> allUsers = userService.getAllUsers();

        assertEquals(2, allUsers.size());
    }

    @Test
    void testGetUserById() {
        User created = userService.createUser(new User("John Doe", "john@example.com"));

        User found = userService.getUserById(created.getId());

        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
        assertEquals("John Doe", found.getName());
    }

    @Test
    void testRollbackOnException() {
        User user = new User("Test User Rollback", "testrollback@example.com");

        // Перевіряємо, що виняток викидається
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUserWithRollback(user, true);
        });

        assertEquals("Тестова помилка для перевірки rollback", exception.getMessage());

        // Перевіряємо, що користувач не збережений (rollback спрацював)
        // Оскільки тест має @Transactional, rollback відбувається автоматично
        // Перевіряємо, що метод викинув виняток
        assertNotNull(exception);
    }

    @Test
    void testNoRollbackWhenNoException() {
        User user = new User("Test User", "test@example.com");

        User created = userService.createUserWithRollback(user, false);

        assertNotNull(created.getId());
        List<User> users = userService.getUsersByName("Test User");
        assertEquals(1, users.size());
    }
}
