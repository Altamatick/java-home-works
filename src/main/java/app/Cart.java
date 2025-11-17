package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class Cart {
    private final List<Product> items = new ArrayList<>();
    
    @Autowired
    private ProductRepository productRepository;

    public void addProduct(Product product) {
        if (product != null) {
            items.add(product);
            System.out.println("Товар додано до кошика: " + product.getName());
        }
    }
    
    public boolean addProductById(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    items.add(product);
                    System.out.println("Товар додано до кошика: " + product.getName());
                    return true;
                })
                .orElseGet(() -> {
                    System.out.println("Товар з id=" + id + " не знайдено");
                    return false;
                });
    }

    public boolean removeProductById(Long id) {
        boolean removed = items.removeIf(p -> p.getId().equals(id));
        if (removed) {
            System.out.println("Товар з id=" + id + " видалено з кошика");
        } else {
            System.out.println("Товар з id=" + id + " не знайдено в кошику");
        }
        return removed;
    }

    public List<Product> getItems() {
        return new ArrayList<>(items);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getItemCount() {
        return items.size();
    }

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    @Override
    public String toString() {
        if (items.isEmpty()) {
            return "Кошик порожній";
        }
        StringBuilder sb = new StringBuilder("Кошик:\n");
        for (Product product : items) {
            sb.append("  - ").append(product).append("\n");
        }
        sb.append("Всього товарів: ").append(items.size())
          .append(", Загальна сума: ").append(getTotalPrice()).append(" грн");
        return sb.toString();
    }
}

