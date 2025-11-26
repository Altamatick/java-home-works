package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Отримання всіх замовлень
     * GET http://localhost:8080/orders
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    /**
     * Отримання конкретного замовлення
     * GET http://localhost:8080/orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Додавання нового замовлення
     * POST http://localhost:8080/orders
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        // Встановлюємо дату створення
        if (order.getCreatedAt() == null) {
            order.setCreatedAt(LocalDateTime.now());
        }

        // Обчислюємо totalCost якщо не вказано
        if (order.getTotalCost() == null && order.getProducts() != null) {
            double total = order.getProducts().stream()
                    .mapToDouble(Product::getPrice)
                    .sum();
            order.setTotalCost(total);
        }

        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    /**
     * Оновлення замовлення
     * PUT http://localhost:8080/orders/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        Optional<Order> orderOpt = orderRepository.findById(id);

        if (orderOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Order order = orderOpt.get();
        order.setTotalCost(orderDetails.getTotalCost());
        order.setProducts(orderDetails.getProducts());

        // Обчислюємо totalCost якщо не вказано
        if (orderDetails.getTotalCost() == null && orderDetails.getProducts() != null) {
            double total = orderDetails.getProducts().stream()
                    .mapToDouble(Product::getPrice)
                    .sum();
            order.setTotalCost(total);
        }

        Order updatedOrder = orderRepository.save(order);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Видалення замовлення
     * DELETE http://localhost:8080/orders/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
