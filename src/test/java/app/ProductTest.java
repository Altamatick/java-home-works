package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testProductCreation() {
        Product product = new Product(1L, "Ноутбук", 25000.0);

        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("Ноутбук", product.getName());
        assertEquals(25000.0, product.getCost());
    }

    @Test
    void testProductDefaultConstructor() {
        Product product = new Product();

        assertNotNull(product);
        assertNull(product.getId());
        assertNull(product.getName());
        assertNull(product.getCost());
    }

    @Test
    void testProductSetters() {
        Product product = new Product();

        product.setId(2L);
        product.setName("Миша");
        product.setCost(800.0);

        assertEquals(2L, product.getId());
        assertEquals("Миша", product.getName());
        assertEquals(800.0, product.getCost());
    }

    @Test
    void testProductEquals() {
        Product product1 = new Product(1L, "Ноутбук", 25000.0);
        Product product2 = new Product(1L, "Ноутбук", 25000.0);
        Product product3 = new Product(2L, "Миша", 800.0);

        assertEquals(product1, product2);
        assertNotEquals(product1, product3);
    }

    @Test
    void testProductHashCode() {
        Product product1 = new Product(1L, "Ноутбук", 25000.0);
        Product product2 = new Product(1L, "Ноутбук", 25000.0);

        assertEquals(product1.hashCode(), product2.hashCode());
    }

    @Test
    void testProductToString() {
        Product product = new Product(1L, "Ноутбук", 25000.0);
        String toString = product.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name='Ноутбук'"));
        assertTrue(toString.contains("cost=25000.0"));
    }
}
