package app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void testOrderRepositoryBean() {
        OrderRepository repository = applicationContext.getBean(OrderRepository.class);
        assertNotNull(repository);
    }

    @Test
    void testPingControllerBean() {
        PingController controller = applicationContext.getBean(PingController.class);
        assertNotNull(controller);
    }

    @Test
    void testOrderControllerBean() {
        OrderController controller = applicationContext.getBean(OrderController.class);
        assertNotNull(controller);
    }
}
