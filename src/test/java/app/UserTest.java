package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User("John Doe", "john@example.com", "555-1234", "password123");

        assertNotNull(user);
        assertNull(user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("555-1234", user.getPhone());
        assertEquals("password123", user.getPassword());
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
    }

    @Test
    void testUserSetters() {
        User user = new User();

        user.setId(1L);
        user.setName("Jane Doe");
        user.setEmail("jane@example.com");
        user.setPhone("555-5678");
        user.setPassword("password456");

        assertEquals(1L, user.getId());
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals("555-5678", user.getPhone());
        assertEquals("password456", user.getPassword());
    }

    @Test
    void testAddRole() {
        User user = new User("John Doe", "john@example.com", "555-1234", "password123");
        Role role = new Role("USER");

        user.addRole(role);

        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(role));
        assertTrue(role.getUsers().contains(user));
    }

    @Test
    void testRemoveRole() {
        User user = new User("John Doe", "john@example.com", "555-1234", "password123");
        Role role = new Role("USER");

        user.addRole(role);
        assertEquals(1, user.getRoles().size());

        user.removeRole(role);

        assertEquals(0, user.getRoles().size());
        assertFalse(role.getUsers().contains(user));
    }

    @Test
    void testUserEquals() {
        User user1 = new User("John Doe", "john@example.com", "555-1234", "password123");
        user1.setId(1L);
        User user2 = new User("John Doe", "john@example.com", "555-1234", "password123");
        user2.setId(1L);
        User user3 = new User("Jane Doe", "jane@example.com", "555-5678", "password456");
        user3.setId(2L);

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }

    @Test
    void testUserHashCode() {
        User user1 = new User("John Doe", "john@example.com", "555-1234", "password123");
        user1.setId(1L);
        User user2 = new User("John Doe", "john@example.com", "555-1234", "password123");
        user2.setId(1L);

        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
