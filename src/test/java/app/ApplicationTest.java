package app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
class ApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void testUserRepositoryBean() {
        UserRepository repository = applicationContext.getBean(UserRepository.class);
        assertNotNull(repository);
    }

    @Test
    void testPostRepositoryBean() {
        PostRepository repository = applicationContext.getBean(PostRepository.class);
        assertNotNull(repository);
    }

    @Test
    void testUserServiceBean() {
        UserService service = applicationContext.getBean(UserService.class);
        assertNotNull(service);
    }

    @Test
    void testUserControllerBean() {
        UserController controller = applicationContext.getBean(UserController.class);
        assertNotNull(controller);
    }
}
