package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {

    private OrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new OrderRepository();
        repository.init();
    }

    @Test
    void testFindById() {
        Optional<Order> order = repository.findById(1L);

        assertTrue(order.isPresent());
        assertEquals(1L, order.get().getId());
        assertNotNull(order.get().getProducts());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Order> order = repository.findById(999L);

        assertFalse(order.isPresent());
    }

    @Test
    void testFindAll() {
        List<Order> orders = repository.findAll();

        assertNotNull(orders);
        assertEquals(2, orders.size());
    }

    @Test
    void testAddOrder() {
        List<Product> products = Arrays.asList(
            new Product(10L, "Новий товар", 5000.0)
        );
        Order newOrder = new Order();
        newOrder.setProducts(products);

        Order savedOrder = repository.add(newOrder);

        assertNotNull(savedOrder.getId());
        assertNotNull(savedOrder.getCreationDate());
        assertEquals(5000.0, savedOrder.getTotalCost());
    }

    @Test
    void testAddOrderWithId() {
        List<Product> products = Arrays.asList(
            new Product(11L, "Товар з ID", 3000.0)
        );
        Order newOrder = new Order(100L, null, null, products);

        Order savedOrder = repository.add(newOrder);

        assertEquals(100L, savedOrder.getId());
        assertNotNull(savedOrder.getCreationDate());
        assertEquals(3000.0, savedOrder.getTotalCost());
    }

    @Test
    void testAddOrderAutoCalculateTotalCost() {
        List<Product> products = Arrays.asList(
            new Product(12L, "Товар 1", 1000.0),
            new Product(13L, "Товар 2", 2000.0),
            new Product(14L, "Товар 3", 3000.0)
        );
        Order newOrder = new Order();
        newOrder.setProducts(products);

        Order savedOrder = repository.add(newOrder);

        assertEquals(6000.0, savedOrder.getTotalCost());
    }

    @Test
    void testAddOrderWithExistingTotalCost() {
        List<Product> products = Arrays.asList(
            new Product(15L, "Товар", 1000.0)
        );
        Order newOrder = new Order();
        newOrder.setProducts(products);
        newOrder.setTotalCost(1500.0);

        Order savedOrder = repository.add(newOrder);

        assertEquals(1500.0, savedOrder.getTotalCost());
    }

    @Test
    void testAddOrderWithCreationDate() {
        LocalDateTime customDate = LocalDateTime.of(2025, 1, 1, 12, 0);
        List<Product> products = Arrays.asList(
            new Product(16L, "Товар", 1000.0)
        );
        Order newOrder = new Order();
        newOrder.setCreationDate(customDate);
        newOrder.setProducts(products);

        Order savedOrder = repository.add(newOrder);

        assertEquals(customDate, savedOrder.getCreationDate());
    }

    @Test
    void testRepositoryInitialization() {
        List<Order> orders = repository.findAll();

        assertEquals(2, orders.size());
        assertTrue(orders.stream().anyMatch(o -> o.getId().equals(1L)));
        assertTrue(orders.stream().anyMatch(o -> o.getId().equals(2L)));
    }

    @Test
    void testMultipleAdds() {
        int initialSize = repository.findAll().size();

        for (int i = 0; i < 5; i++) {
            Order order = new Order();
            order.setProducts(Arrays.asList(new Product((long) i, "Product " + i, 100.0)));
            repository.add(order);
        }

        assertEquals(initialSize + 5, repository.findAll().size());
    }
}
