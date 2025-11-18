package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class CustomerDaoTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("test_db")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // Створюємо таблицю, якщо її немає
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS customer (
                id BIGSERIAL PRIMARY KEY,
                full_name VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL UNIQUE,
                social_security_number VARCHAR(50) NOT NULL UNIQUE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """);
        // Очищаємо таблицю перед кожним тестом
        jdbcTemplate.execute("TRUNCATE TABLE customer RESTART IDENTITY CASCADE");
    }

    @Test
    void testAddCustomer() {
        Customer customer = new Customer("Іван Петренко", "ivan@example.com", "1234567890");

        Customer saved = customerDao.add(customer);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("Іван Петренко", saved.getFullName());
        assertEquals("ivan@example.com", saved.getEmail());
        assertEquals("1234567890", saved.getSocialSecurityNumber());
    }

    @Test
    void testFindById() {
        Customer customer = new Customer("Марія Коваленко", "maria@example.com", "0987654321");
        Customer saved = customerDao.add(customer);

        Optional<Customer> found = customerDao.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Марія Коваленко", found.get().getFullName());
        assertEquals("maria@example.com", found.get().getEmail());
        assertEquals("0987654321", found.get().getSocialSecurityNumber());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Customer> found = customerDao.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateCustomer() {
        Customer customer = new Customer("Петро Сидоренко", "petro@example.com", "1122334455");
        Customer saved = customerDao.add(customer);

        saved.setFullName("Петро Олексійович Сидоренко");
        saved.setEmail("petro.new@example.com");

        boolean updated = customerDao.update(saved);

        assertTrue(updated);

        Optional<Customer> found = customerDao.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Петро Олексійович Сидоренко", found.get().getFullName());
        assertEquals("petro.new@example.com", found.get().getEmail());
    }

    @Test
    void testUpdateNonExistentCustomer() {
        Customer customer = new Customer(999L, "Неіснуючий", "none@example.com", "9999999999");

        boolean updated = customerDao.update(customer);

        assertFalse(updated);
    }

    @Test
    void testDeleteById() {
        Customer customer = new Customer("Олена Мельник", "olena@example.com", "2233445566");
        Customer saved = customerDao.add(customer);

        boolean deleted = customerDao.deleteById(saved.getId());

        assertTrue(deleted);

        Optional<Customer> found = customerDao.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteByIdNotFound() {
        boolean deleted = customerDao.deleteById(999L);

        assertFalse(deleted);
    }

    @Test
    void testFindAll() {
        customerDao.add(new Customer("Клієнт 1", "client1@example.com", "1111111111"));
        customerDao.add(new Customer("Клієнт 2", "client2@example.com", "2222222222"));
        customerDao.add(new Customer("Клієнт 3", "client3@example.com", "3333333333"));

        List<Customer> allCustomers = customerDao.findAll();

        assertEquals(3, allCustomers.size());
        assertTrue(allCustomers.stream().anyMatch(c -> c.getEmail().equals("client1@example.com")));
        assertTrue(allCustomers.stream().anyMatch(c -> c.getEmail().equals("client2@example.com")));
        assertTrue(allCustomers.stream().anyMatch(c -> c.getEmail().equals("client3@example.com")));
    }

    @Test
    void testFindAllEmpty() {
        List<Customer> allCustomers = customerDao.findAll();

        assertTrue(allCustomers.isEmpty());
    }

    @Test
    void testFullCrudCycle() {
        // Create
        Customer customer = new Customer("Тестовий Клієнт", "test@example.com", "5555555555");
        Customer saved = customerDao.add(customer);
        Long id = saved.getId();

        // Read
        Optional<Customer> found = customerDao.findById(id);
        assertTrue(found.isPresent());
        assertEquals("Тестовий Клієнт", found.get().getFullName());

        // Update
        saved.setFullName("Оновлений Клієнт");
        customerDao.update(saved);

        Optional<Customer> updated = customerDao.findById(id);
        assertTrue(updated.isPresent());
        assertEquals("Оновлений Клієнт", updated.get().getFullName());

        // Delete
        customerDao.deleteById(id);
        Optional<Customer> deleted = customerDao.findById(id);
        assertFalse(deleted.isPresent());
    }
}
