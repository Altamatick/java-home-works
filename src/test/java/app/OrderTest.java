package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    private Order order;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product("Laptop", 1200.0);
        product2 = new Product("Mouse", 25.0);
        order = new Order();
    }

    @Test
    void constructor_ShouldInitializeWithDefaultValues() {
        Order newOrder = new Order();

        assertThat(newOrder.getId()).isNull();
        assertThat(newOrder.getProducts()).isNotNull();
        assertThat(newOrder.getCreatedAt()).isNotNull();
    }

    @Test
    void constructor_ShouldInitializeWithParameters() {
        List<Product> products = Arrays.asList(product1, product2);
        Order newOrder = new Order(1225.0, products);

        assertThat(newOrder.getTotalCost()).isEqualTo(1225.0);
        assertThat(newOrder.getProducts()).hasSize(2);
        assertThat(newOrder.getCreatedAt()).isNotNull();
    }

    @Test
    void constructor_ShouldInitializeWithNullProducts() {
        Order newOrder = new Order(100.0, null);

        assertThat(newOrder.getProducts()).isNotNull();
        assertThat(newOrder.getProducts()).isEmpty();
    }

    @Test
    void setId_ShouldSetId() {
        order.setId(1L);

        assertThat(order.getId()).isEqualTo(1L);
    }

    @Test
    void setTotalCost_ShouldSetTotalCost() {
        order.setTotalCost(1500.0);

        assertThat(order.getTotalCost()).isEqualTo(1500.0);
    }

    @Test
    void setProducts_ShouldSetProducts() {
        List<Product> products = Arrays.asList(product1, product2);
        order.setProducts(products);

        assertThat(order.getProducts()).hasSize(2);
        assertThat(order.getProducts()).containsExactly(product1, product2);
    }

    @Test
    void setCreatedAt_ShouldSetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        order.setCreatedAt(now);

        assertThat(order.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void equals_ShouldReturnTrue_WhenOrdersHaveSameId() {
        order.setId(1L);
        Order otherOrder = new Order();
        otherOrder.setId(1L);

        assertThat(order).isEqualTo(otherOrder);
    }

    @Test
    void equals_ShouldReturnFalse_WhenOrdersHaveDifferentId() {
        order.setId(1L);
        Order otherOrder = new Order();
        otherOrder.setId(2L);

        assertThat(order).isNotEqualTo(otherOrder);
    }

    @Test
    void equals_ShouldReturnTrue_WhenComparingWithItself() {
        assertThat(order).isEqualTo(order);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparingWithNull() {
        assertThat(order.equals(null)).isFalse();
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparingWithDifferentClass() {
        assertThat(order.equals(new String("test"))).isFalse();
    }

    @Test
    void hashCode_ShouldReturnSameValue_ForOrdersWithSameId() {
        order.setId(1L);
        Order otherOrder = new Order();
        otherOrder.setId(1L);

        assertThat(order.hashCode()).isEqualTo(otherOrder.hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectFormat() {
        order.setId(1L);
        order.setTotalCost(1225.0);
        order.setProducts(Arrays.asList(product1, product2));

        String result = order.toString();

        assertThat(result).contains("id=1");
        assertThat(result).contains("totalCost=1225.0");
        assertThat(result).contains("productsCount=2");
        assertThat(result).contains("createdAt=");
    }

    @Test
    void toString_ShouldHandleNullProducts() {
        order.setId(1L);
        order.setTotalCost(100.0);
        order.setProducts(null);

        String result = order.toString();

        assertThat(result).contains("productsCount=0");
    }
}

