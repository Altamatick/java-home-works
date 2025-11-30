package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    void testPostCreation() {
        Post post = new Post("Test Title", "Test Content");

        assertNotNull(post);
        assertNull(post.getId());
        assertEquals("Test Title", post.getTitle());
        assertEquals("Test Content", post.getContent());
        assertNull(post.getUser());
    }

    @Test
    void testPostSetters() {
        Post post = new Post();

        post.setId(1L);
        post.setTitle("New Title");
        post.setContent("New Content");

        assertEquals(1L, post.getId());
        assertEquals("New Title", post.getTitle());
        assertEquals("New Content", post.getContent());
    }

    @Test
    void testPostWithUser() {
        User user = new User("John Doe", "john@example.com");
        Post post = new Post("Title", "Content");

        post.setUser(user);

        assertEquals(user, post.getUser());
    }

    @Test
    void testPostEquals() {
        Post post1 = new Post("Title", "Content");
        post1.setId(1L);
        Post post2 = new Post("Title", "Content");
        post2.setId(1L);
        Post post3 = new Post("Title", "Content");
        post3.setId(2L);

        assertEquals(post1, post2);
        assertNotEquals(post1, post3);
    }

    @Test
    void testPostHashCode() {
        Post post1 = new Post("Title", "Content");
        post1.setId(1L);
        Post post2 = new Post("Title", "Content");
        post2.setId(1L);

        assertEquals(post1.hashCode(), post2.hashCode());
    }

    @Test
    void testPostToString() {
        Post post = new Post("Test Title", "Test Content");
        post.setId(1L);
        String toString = post.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("title='Test Title'"));
        assertTrue(toString.contains("content='Test Content'"));
    }
}
