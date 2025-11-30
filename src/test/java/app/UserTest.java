package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User("John Doe", "john@example.com");

        assertNotNull(user);
        assertNull(user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertNotNull(user.getPosts());
        assertTrue(user.getPosts().isEmpty());
    }

    @Test
    void testUserSetters() {
        User user = new User();

        user.setId(1L);
        user.setName("Jane Doe");
        user.setEmail("jane@example.com");

        assertEquals(1L, user.getId());
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane@example.com", user.getEmail());
    }

    @Test
    void testAddPost() {
        User user = new User("John Doe", "john@example.com");
        Post post = new Post("Title", "Content");

        user.addPost(post);

        assertEquals(1, user.getPosts().size());
        assertEquals(post, user.getPosts().get(0));
        assertEquals(user, post.getUser());
    }

    @Test
    void testRemovePost() {
        User user = new User("John Doe", "john@example.com");
        Post post = new Post("Title", "Content");

        user.addPost(post);
        assertEquals(1, user.getPosts().size());

        user.removePost(post);

        assertEquals(0, user.getPosts().size());
        assertNull(post.getUser());
    }

    @Test
    void testUserEquals() {
        User user1 = new User("John Doe", "john@example.com");
        user1.setId(1L);
        User user2 = new User("John Doe", "john@example.com");
        user2.setId(1L);
        User user3 = new User("Jane Doe", "jane@example.com");
        user3.setId(2L);

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }

    @Test
    void testUserHashCode() {
        User user1 = new User("John Doe", "john@example.com");
        user1.setId(1L);
        User user2 = new User("John Doe", "john@example.com");
        user2.setId(1L);

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testUserToString() {
        User user = new User("John Doe", "john@example.com");
        user.setId(1L);
        String toString = user.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name='John Doe'"));
        assertTrue(toString.contains("email='john@example.com'"));
    }
}
