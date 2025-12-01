package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Створити базові ролі
        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));
    }

    @Test
    void testRegisterUser() {
        User user = userService.registerUser("John Doe", "john@example.com", "555-1234", "password123");

        assertNotNull(user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("555-1234", user.getPhone());
        assertTrue(passwordEncoder.matches("password123", user.getPassword()));
        assertFalse(user.getRoles().isEmpty());
        assertTrue(user.getRoles().stream().anyMatch(r -> r.getName().equals("USER")));
    }

    @Test
    void testRegisterUserDuplicateEmail() {
        userService.registerUser("John Doe", "john@example.com", "555-1234", "password123");

        assertThrows(RuntimeException.class, () -> {
            userService.registerUser("Jane Doe", "john@example.com", "555-5678", "password456");
        });
    }

    @Test
    void testFindByEmail() {
        userService.registerUser("John Doe", "john@example.com", "555-1234", "password123");

        User found = userService.findByEmail("john@example.com");

        assertNotNull(found);
        assertEquals("John Doe", found.getName());
    }

    @Test
    void testFindByEmailNotFound() {
        assertThrows(RuntimeException.class, () -> {
            userService.findByEmail("nonexistent@example.com");
        });
    }

    @Test
    void testGetAllUsers() {
        userService.registerUser("User1", "user1@example.com", "555-1111", "pass1");
        userService.registerUser("User2", "user2@example.com", "555-2222", "pass2");

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void testPasswordHashing() {
        User user = userService.registerUser("John Doe", "john@example.com", "555-1234", "password123");

        // Пароль має бути хешований, а не зберігатися як plain text
        assertNotEquals("password123", user.getPassword());
        assertTrue(user.getPassword().startsWith("$2a$") || user.getPassword().startsWith("$2b$"));
    }
}
