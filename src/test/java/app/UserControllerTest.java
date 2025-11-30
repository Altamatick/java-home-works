package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testCreateUser() throws Exception {
        User user = new User("John Doe", "john@example.com");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testCreateUserWithPosts() throws Exception {
        UserController.CreateUserWithPostsRequest request =
            new UserController.CreateUserWithPostsRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPosts(Arrays.asList(
            new Post("Post 1", "Content 1"),
            new Post("Post 2", "Content 2")
        ));

        mockMvc.perform(post("/users/with-posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.posts").isArray())
                .andExpect(jsonPath("$.posts.length()").value(2));
    }

    @Test
    void testGetUsersByName() throws Exception {
        userRepository.save(new User("John Doe", "john1@example.com"));
        userRepository.save(new User("John Doe", "john2@example.com"));

        mockMvc.perform(get("/users/by-name")
                .param("name", "John Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testGetUsersByEmailDomain() throws Exception {
        userRepository.save(new User("User1", "user1@gmail.com"));
        userRepository.save(new User("User2", "user2@gmail.com"));
        userRepository.save(new User("User3", "user3@yahoo.com"));

        mockMvc.perform(get("/users/by-email-domain")
                .param("domain", "@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].email").value("user1@gmail.com"));
    }

    @Test
    void testGetPostsByUserId() throws Exception {
        User user = userRepository.save(new User("John Doe", "john@example.com"));
        Post post1 = new Post("Post 1", "Content 1");
        post1.setUser(user);
        postRepository.save(post1);
        Post post2 = new Post("Post 2", "Content 2");
        post2.setUser(user);
        postRepository.save(post2);

        mockMvc.perform(get("/users/{userId}/posts", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").exists());
    }

    @Test
    void testGetAllUsers() throws Exception {
        userRepository.save(new User("User1", "user1@example.com"));
        userRepository.save(new User("User2", "user2@example.com"));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetUserById() throws Exception {
        User user = userRepository.save(new User("John Doe", "john@example.com"));

        mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testTestRollback() throws Exception {
        User user = new User("Test Rollback User", "testrollback@example.com");

        String response = mockMvc.perform(post("/users/test-rollback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(response.contains("Rollback"), "Відповідь має містити інформацію про rollback");
    }

    @Test
    void testGetPostsByUserIdEmpty() throws Exception {
        User user = userRepository.save(new User("John Doe", "john@example.com"));

        mockMvc.perform(get("/users/{userId}/posts", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
