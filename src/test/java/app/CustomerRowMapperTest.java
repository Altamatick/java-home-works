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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class CustomerRowMapperTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("test_db_mapper")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CustomerRowMapper rowMapper;

    @BeforeEach
    void setUp() {
        rowMapper = new CustomerRowMapper();
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
        jdbcTemplate.execute("TRUNCATE TABLE customer RESTART IDENTITY CASCADE");
    }

    @Test
    void testMapRow() {
        // Вставляємо тестові дані
        jdbcTemplate.update(
            "INSERT INTO customer (full_name, email, social_security_number) VALUES (?, ?, ?)",
            "Іван Петренко", "ivan@example.com", "1234567890"
        );

        // Отримуємо дані через маппер
        List<Customer> customers = jdbcTemplate.query(
            "SELECT id, full_name, email, social_security_number FROM customer WHERE id = 1",
            rowMapper
        );

        // Перевіряємо результат
        assertEquals(1, customers.size());
        Customer customer = customers.get(0);
        assertNotNull(customer);
        assertEquals(1L, customer.getId());
        assertEquals("Іван Петренко", customer.getFullName());
        assertEquals("ivan@example.com", customer.getEmail());
        assertEquals("1234567890", customer.getSocialSecurityNumber());
    }

    @Test
    void testMapRowWithMultipleRows() {
        // Вставляємо кілька записів
        jdbcTemplate.update(
            "INSERT INTO customer (full_name, email, social_security_number) VALUES (?, ?, ?)",
            "Клієнт 1", "client1@example.com", "1111111111"
        );
        jdbcTemplate.update(
            "INSERT INTO customer (full_name, email, social_security_number) VALUES (?, ?, ?)",
            "Клієнт 2", "client2@example.com", "2222222222"
        );

        // Отримуємо всі дані через маппер
        List<Customer> customers = jdbcTemplate.query(
            "SELECT id, full_name, email, social_security_number FROM customer ORDER BY id",
            rowMapper
        );

        // Перевіряємо результат
        assertEquals(2, customers.size());
        assertEquals("Клієнт 1", customers.get(0).getFullName());
        assertEquals("Клієнт 2", customers.get(1).getFullName());
    }
}
