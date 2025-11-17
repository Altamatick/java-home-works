package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testFindAll() {
        List<Product> products = productRepository.findAll();
        
        assertNotNull(products);
        assertFalse(products.isEmpty());
        assertEquals(8, products.size());
    }

    @Test
    void testFindById() {
        Optional<Product> product = productRepository.findById(1L);
        
        assertTrue(product.isPresent());
        assertEquals("Ноутбук", product.get().getName());
        assertEquals(25000.0, product.get().getPrice());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Product> product = productRepository.findById(999L);
        
        assertFalse(product.isPresent());
    }

    @Test
    void testSave() {
        Product newProduct = new Product(100L, "Тестовий товар", 5000.0);
        Product saved = productRepository.save(newProduct);
        
        assertNotNull(saved);
        assertEquals(100L, saved.getId());
        assertEquals("Тестовий товар", saved.getName());
        
        Optional<Product> found = productRepository.findById(100L);
        assertTrue(found.isPresent());
    }

    @Test
    void testUpdate() {
        Product updatedProduct = new Product(1L, "Оновлений ноутбук", 30000.0);
        Product result = productRepository.update(1L, updatedProduct);
        
        assertNotNull(result);
        assertEquals("Оновлений ноутбук", result.getName());
        assertEquals(30000.0, result.getPrice());
        
        // Перевіряємо, що зміни збереглися
        Optional<Product> found = productRepository.findById(1L);
        assertTrue(found.isPresent());
        assertEquals("Оновлений ноутбук", found.get().getName());
        
        // Повертаємо оригінальне значення для інших тестів
        productRepository.update(1L, new Product(1L, "Ноутбук", 25000.0));
    }

    @Test
    void testUpdateNotFound() {
        Product updatedProduct = new Product(999L, "Неіснуючий товар", 1000.0);
        Product result = productRepository.update(999L, updatedProduct);
        
        assertNull(result);
    }

    @Test
    void testDeleteById() {
        // Спочатку додаємо тестовий товар
        Product testProduct = new Product(200L, "Товар для видалення", 1000.0);
        productRepository.save(testProduct);
        
        // Перевіряємо, що він існує
        assertTrue(productRepository.findById(200L).isPresent());
        
        // Видаляємо
        boolean deleted = productRepository.deleteById(200L);
        
        assertTrue(deleted);
        assertFalse(productRepository.findById(200L).isPresent());
    }

    @Test
    void testDeleteByIdNotFound() {
        boolean deleted = productRepository.deleteById(999L);
        
        assertFalse(deleted);
    }

    @Test
    void testRepositoryInitialization() {
        List<Product> products = productRepository.findAll();
        
        // Перевіряємо, що репозиторій ініціалізований з правильними даними
        assertTrue(products.size() >= 8);
        
        // Перевіряємо наявність деяких товарів
        assertTrue(productRepository.findById(1L).isPresent());
        assertTrue(productRepository.findById(2L).isPresent());
        assertTrue(productRepository.findById(8L).isPresent());
    }
}

