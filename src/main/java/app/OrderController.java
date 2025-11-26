package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

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
     * Отримання всіх замовлень
     * GET http://localhost:8080/orders
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    /**
     * Додавання нового замовлення
     * POST http://localhost:8080/orders
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order savedOrder = orderRepository.add(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }
}
