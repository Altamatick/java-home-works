package app;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testOrderCreation() {
        LocalDateTime now = LocalDateTime.now();
        List<Product> products = Arrays.asList(
            new Product(1L, "Ноутбук", 25000.0),
            new Product(2L, "Миша", 800.0)
        );

        Order order = new Order(1L, now, 25800.0, products);

        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals(now, order.getCreationDate());
        assertEquals(25800.0, order.getTotalCost());
        assertEquals(2, order.getProducts().size());
    }

    @Test
    void testOrderDefaultConstructor() {
        Order order = new Order();

        assertNotNull(order);
        assertNull(order.getId());
        assertNull(order.getCreationDate());
        assertNull(order.getTotalCost());
        assertNotNull(order.getProducts());
        assertTrue(order.getProducts().isEmpty());
    }

    @Test
    void testOrderSetters() {
        Order order = new Order();
        LocalDateTime now = LocalDateTime.now();
        List<Product> products = Arrays.asList(
            new Product(1L, "Товар", 1000.0)
        );

        order.setId(5L);
        order.setCreationDate(now);
        order.setTotalCost(1000.0);
        order.setProducts(products);

        assertEquals(5L, order.getId());
        assertEquals(now, order.getCreationDate());
        assertEquals(1000.0, order.getTotalCost());
        assertEquals(1, order.getProducts().size());
    }

    @Test
    void testOrderEquals() {
        LocalDateTime now = LocalDateTime.now();
        List<Product> products = Arrays.asList(
            new Product(1L, "Товар", 1000.0)
        );

        Order order1 = new Order(1L, now, 1000.0, products);
        Order order2 = new Order(1L, now, 1000.0, products);
        Order order3 = new Order(2L, now, 1000.0, products);

        assertEquals(order1, order2);
        assertNotEquals(order1, order3);
    }

    @Test
    void testOrderHashCode() {
        LocalDateTime now = LocalDateTime.now();
        List<Product> products = Arrays.asList(
            new Product(1L, "Товар", 1000.0)
        );

        Order order1 = new Order(1L, now, 1000.0, products);
        Order order2 = new Order(1L, now, 1000.0, products);

        assertEquals(order1.hashCode(), order2.hashCode());
    }

    @Test
    void testOrderToString() {
        LocalDateTime now = LocalDateTime.now();
        List<Product> products = Arrays.asList(
            new Product(1L, "Товар", 1000.0)
        );

        Order order = new Order(1L, now, 1000.0, products);
        String toString = order.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("totalCost=1000.0"));
    }

    @Test
    void testOrderWithNullProducts() {
        Order order = new Order(1L, LocalDateTime.now(), 0.0, null);

        assertNotNull(order.getProducts());
        assertTrue(order.getProducts().isEmpty());
    }
}
