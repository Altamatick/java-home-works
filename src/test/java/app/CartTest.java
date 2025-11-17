package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ProductRepository productRepository;

    private Cart cart;

    @BeforeEach
    void setUp() {
        // Отримуємо новий екземпляр кошика (prototype scope)
        cart = applicationContext.getBean(Cart.class);
    }

    @Test
    void testCartIsPrototype() {
        Cart cart1 = applicationContext.getBean(Cart.class);
        Cart cart2 = applicationContext.getBean(Cart.class);
        
        // Перевіряємо, що це різні екземпляри
        assertNotSame(cart1, cart2);
    }

    @Test
    void testCartIsEmptyInitially() {
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getItemCount());
        assertEquals(0.0, cart.getTotalPrice());
    }

    @Test
    void testAddProduct() {
        Product product = new Product(1L, "Ноутбук", 25000.0);
        cart.addProduct(product);
        
        assertFalse(cart.isEmpty());
        assertEquals(1, cart.getItemCount());
        assertEquals(25000.0, cart.getTotalPrice());
    }

    @Test
    void testAddProductById() {
        boolean added = cart.addProductById(1L);
        
        assertTrue(added);
        assertFalse(cart.isEmpty());
        assertEquals(1, cart.getItemCount());
        
        List<Product> items = cart.getItems();
        assertEquals(1, items.size());
        assertEquals("Ноутбук", items.get(0).getName());
    }

    @Test
    void testAddProductByIdNotFound() {
        boolean added = cart.addProductById(999L);
        
        assertFalse(added);
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getItemCount());
    }

    @Test
    void testAddMultipleProducts() {
        cart.addProductById(1L);
        cart.addProductById(2L);
        cart.addProductById(3L);
        
        assertEquals(3, cart.getItemCount());
        assertEquals(52000.0, cart.getTotalPrice()); // 25000 + 15000 + 12000
    }

    @Test
    void testRemoveProductById() {
        cart.addProductById(1L);
        cart.addProductById(2L);
        
        assertEquals(2, cart.getItemCount());
        
        boolean removed = cart.removeProductById(1L);
        
        assertTrue(removed);
        assertEquals(1, cart.getItemCount());
        assertEquals(15000.0, cart.getTotalPrice());
        
        List<Product> items = cart.getItems();
        assertEquals(1, items.size());
        assertEquals(2L, items.get(0).getId());
    }

    @Test
    void testRemoveProductByIdNotFound() {
        cart.addProductById(1L);
        
        boolean removed = cart.removeProductById(999L);
        
        assertFalse(removed);
        assertEquals(1, cart.getItemCount());
    }

    @Test
    void testRemoveFromEmptyCart() {
        boolean removed = cart.removeProductById(1L);
        
        assertFalse(removed);
        assertTrue(cart.isEmpty());
    }

    @Test
    void testClearCart() {
        cart.addProductById(1L);
        cart.addProductById(2L);
        
        assertEquals(2, cart.getItemCount());
        
        cart.clear();
        
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getItemCount());
        assertEquals(0.0, cart.getTotalPrice());
    }

    @Test
    void testGetItemsReturnsCopy() {
        cart.addProductById(1L);
        
        List<Product> items1 = cart.getItems();
        List<Product> items2 = cart.getItems();
        
        // Перевіряємо, що це різні списки (копії)
        assertNotSame(items1, items2);
        
        // Але вміст однаковий
        assertEquals(items1.size(), items2.size());
        assertEquals(items1.get(0).getId(), items2.get(0).getId());
    }

    @Test
    void testCartToStringEmpty() {
        String toString = cart.toString();
        
        assertEquals("Кошик порожній", toString);
    }

    @Test
    void testCartToStringWithItems() {
        cart.addProductById(1L);
        cart.addProductById(2L);
        
        String toString = cart.toString();
        
        assertTrue(toString.contains("Кошик:"));
        assertTrue(toString.contains("Всього товарів: 2"));
        assertTrue(toString.contains("Загальна сума: 40000.0"));
    }

    @Test
    void testAddNullProduct() {
        cart.addProduct(null);
        
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getItemCount());
    }

    @Test
    void testTotalPriceCalculation() {
        cart.addProductById(1L); // 25000
        cart.addProductById(4L); // 3000
        cart.addProductById(6L); // 800
        
        assertEquals(28800.0, cart.getTotalPrice());
    }
}

