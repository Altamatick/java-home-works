package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product("Laptop", 1200.0);
        product2 = new Product("Mouse", 25.0);
    }

    @Test
    void save_ShouldPersistOrder() {
        Order order = new Order();
        order.setTotalCost(1225.0);
        order.setProducts(Arrays.asList(product1, product2));
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getTotalCost()).isEqualTo(1225.0);
        assertThat(savedOrder.getProducts()).hasSize(2);
    }

    @Test
    void findById_ShouldReturnOrder_WhenOrderExists() {
        Order order = new Order();
        order.setTotalCost(1200.0);
        order.setProducts(Arrays.asList(product1));
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = entityManager.persistAndFlush(order);
        Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId());

        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getId()).isEqualTo(savedOrder.getId());
        assertThat(foundOrder.get().getTotalCost()).isEqualTo(1200.0);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenOrderDoesNotExist() {
        Optional<Order> foundOrder = orderRepository.findById(999L);

        assertThat(foundOrder).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllOrders() {
        Order order1 = new Order();
        order1.setTotalCost(1200.0);
        order1.setProducts(Arrays.asList(product1));
        order1.setCreatedAt(LocalDateTime.now());

        Order order2 = new Order();
        order2.setTotalCost(25.0);
        order2.setProducts(Arrays.asList(product2));
        order2.setCreatedAt(LocalDateTime.now());

        entityManager.persist(order1);
        entityManager.persist(order2);
        entityManager.flush();

        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(Order::getTotalCost)
                .containsExactlyInAnyOrder(1200.0, 25.0);
    }

    @Test
    void deleteById_ShouldRemoveOrder() {
        Order order = new Order();
        order.setTotalCost(1200.0);
        order.setProducts(Arrays.asList(product1));
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = entityManager.persistAndFlush(order);
        Long orderId = savedOrder.getId();

        orderRepository.deleteById(orderId);
        Optional<Order> deletedOrder = orderRepository.findById(orderId);

        assertThat(deletedOrder).isEmpty();
    }

    @Test
    void existsById_ShouldReturnTrue_WhenOrderExists() {
        Order order = new Order();
        order.setTotalCost(1200.0);
        order.setProducts(Arrays.asList(product1));
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = entityManager.persistAndFlush(order);

        boolean exists = orderRepository.existsById(savedOrder.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void existsById_ShouldReturnFalse_WhenOrderDoesNotExist() {
        boolean exists = orderRepository.existsById(999L);

        assertThat(exists).isFalse();
    }

    @Test
    void save_ShouldUpdateOrder_WhenOrderExists() {
        Order order = new Order();
        order.setTotalCost(1200.0);
        order.setProducts(Arrays.asList(product1));
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = entityManager.persistAndFlush(order);
        entityManager.clear(); // Clear the persistence context

        Order orderToUpdate = orderRepository.findById(savedOrder.getId()).get();
        orderToUpdate.setTotalCost(1500.0);

        Order updatedOrder = orderRepository.save(orderToUpdate);

        assertThat(updatedOrder.getId()).isEqualTo(savedOrder.getId());
        assertThat(updatedOrder.getTotalCost()).isEqualTo(1500.0);
    }
}

