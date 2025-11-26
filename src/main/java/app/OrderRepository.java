package app;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class OrderRepository {
    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        // Ініціалізуємо репозиторій тестовими даними
        List<Product> products1 = Arrays.asList(
            new Product(1L, "Ноутбук", 25000.0),
            new Product(2L, "Миша", 800.0)
        );
        Order order1 = new Order(1L, LocalDateTime.now().minusDays(2), 25800.0, products1);
        orders.put(1L, order1);
        idGenerator.set(2L);

        List<Product> products2 = Arrays.asList(
            new Product(3L, "Клавіатура", 2000.0),
            new Product(4L, "Монітор", 8000.0)
        );
        Order order2 = new Order(2L, LocalDateTime.now().minusDays(1), 10000.0, products2);
        orders.put(2L, order2);
        idGenerator.set(3L);
    }

    /**
     * Отримання замовлення по id
     */
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orders.get(id));
    }

    /**
     * Отримання всіх замовлень
     */
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    /**
     * Додавання замовлення
     */
    public Order add(Order order) {
        if (order.getId() == null) {
            order.setId(idGenerator.getAndIncrement());
        }
        if (order.getCreationDate() == null) {
            order.setCreationDate(LocalDateTime.now());
        }
        // Обчислюємо totalCost якщо не вказано
        if (order.getTotalCost() == null && order.getProducts() != null) {
            double total = order.getProducts().stream()
                    .mapToDouble(Product::getCost)
                    .sum();
            order.setTotalCost(total);
        }
        orders.put(order.getId(), order);
        return order;
    }
}
