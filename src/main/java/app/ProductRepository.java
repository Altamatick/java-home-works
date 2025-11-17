package app;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final List<Product> products = new ArrayList<>();

    @PostConstruct
    public void init() {
        products.add(new Product(1L, "Ноутбук", 25000.0));
        products.add(new Product(2L, "Смартфон", 15000.0));
        products.add(new Product(3L, "Планшет", 12000.0));
        products.add(new Product(4L, "Навушники", 3000.0));
        products.add(new Product(5L, "Клавіатура", 2000.0));
        products.add(new Product(6L, "Миша", 800.0));
        products.add(new Product(7L, "Монітор", 8000.0));
        products.add(new Product(8L, "Веб-камера", 2500.0));
    }

    // Create
    public Product save(Product product) {
        products.add(product);
        return product;
    }

    // Read
    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    public Optional<Product> findById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    // Update
    public Product update(Long id, Product updatedProduct) {
        Optional<Product> productOpt = findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            return product;
        }
        return null;
    }

    // Delete
    public boolean deleteById(Long id) {
        return products.removeIf(p -> p.getId().equals(id));
    }
}

