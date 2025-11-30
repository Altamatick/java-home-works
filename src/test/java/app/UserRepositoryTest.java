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
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testSaveUser() {
        User user = new User("John Doe", "john@example.com");

        User saved = userRepository.save(user);

        assertNotNull(saved.getId());
        assertEquals("John Doe", saved.getName());
        assertEquals("john@example.com", saved.getEmail());
    }

    @Test
    void testFindByName() {
        userRepository.save(new User("John Doe", "john@example.com"));
        userRepository.save(new User("John Smith", "john.smith@example.com"));
        userRepository.save(new User("Jane Doe", "jane@example.com"));

        List<User> users = userRepository.findByName("John Doe");

        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
    }

    @Test
    void testFindByNameMultipleResults() {
        userRepository.save(new User("John Doe", "john1@example.com"));
        userRepository.save(new User("John Doe", "john2@example.com"));

        List<User> users = userRepository.findByName("John Doe");

        assertEquals(2, users.size());
    }

    @Test
    void testFindByEmailEndingWith() {
        userRepository.save(new User("User1", "user1@gmail.com"));
        userRepository.save(new User("User2", "user2@gmail.com"));
        userRepository.save(new User("User3", "user3@yahoo.com"));

        List<User> gmailUsers = userRepository.findByEmailEndingWith("@gmail.com");

        assertEquals(2, gmailUsers.size());
        assertTrue(gmailUsers.stream().allMatch(u -> u.getEmail().endsWith("@gmail.com")));
    }

    @Test
    void testFindByEmailEndingWithNoResults() {
        userRepository.save(new User("User1", "user1@gmail.com"));

        List<User> yahooUsers = userRepository.findByEmailEndingWith("@yahoo.com");

        assertTrue(yahooUsers.isEmpty());
    }

    @Test
    void testFindAll() {
        userRepository.save(new User("User1", "user1@example.com"));
        userRepository.save(new User("User2", "user2@example.com"));

        List<User> allUsers = userRepository.findAll();

        assertEquals(2, allUsers.size());
    }

    @Test
    void testFindById() {
        User saved = userRepository.save(new User("John Doe", "john@example.com"));

        User found = userRepository.findById(saved.getId()).orElse(null);

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals("John Doe", found.getName());
    }

    @Test
    void testDeleteById() {
        User saved = userRepository.save(new User("John Doe", "john@example.com"));

        userRepository.deleteById(saved.getId());

        assertFalse(userRepository.findById(saved.getId()).isPresent());
    }
}
