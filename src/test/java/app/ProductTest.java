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
        assertEquals(25000.0, product.getPrice());
    }

    @Test
    void testProductSetters() {
        Product product = new Product();
        
        product.setId(2L);
        product.setName("Смартфон");
        product.setPrice(15000.0);
        
        assertEquals(2L, product.getId());
        assertEquals("Смартфон", product.getName());
        assertEquals(15000.0, product.getPrice());
    }

    @Test
    void testProductToString() {
        Product product = new Product(1L, "Ноутбук", 25000.0);
        String toString = product.toString();
        
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name='Ноутбук'"));
        assertTrue(toString.contains("price=25000.0"));
    }
}

