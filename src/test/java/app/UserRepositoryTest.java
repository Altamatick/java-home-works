package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void testSaveUser() {
        User user = new User("John Doe", "john@example.com", "555-1234", "password123");

        User saved = userRepository.save(user);

        assertNotNull(saved.getId());
        assertEquals("John Doe", saved.getName());
        assertEquals("john@example.com", saved.getEmail());
    }

    @Test
    void testFindByEmail() {
        User user = new User("John Doe", "john@example.com", "555-1234", "password123");
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("john@example.com");

        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
    }

    @Test
    void testFindByEmailNotFound() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        assertFalse(found.isPresent());
    }

    @Test
    void testExistsByEmail() {
        User user = new User("John Doe", "john@example.com", "555-1234", "password123");
        userRepository.save(user);

        assertTrue(userRepository.existsByEmail("john@example.com"));
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    void testFindAll() {
        userRepository.save(new User("User1", "user1@example.com", "555-1111", "pass1"));
        userRepository.save(new User("User2", "user2@example.com", "555-2222", "pass2"));

        assertEquals(2, userRepository.findAll().size());
    }
}
