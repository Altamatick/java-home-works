package coffee.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для класу Order.
 */
@DisplayName("Тести замовлення")
class OrderTest {

    @Test
    @DisplayName("Створення замовлення з валідними параметрами")
    void testCreateValidOrder() {
        Order order = new Order(1, "John Doe");

        assertEquals(1, order.getNumber());
        assertEquals("John Doe", order.getName());
    }

    @Test
    @DisplayName("Створення замовлення з порожнім ім'ям викидає виключення")
    void testCreateOrderWithEmptyNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Order(1, ""));
        assertThrows(IllegalArgumentException.class, () -> new Order(1, "   "));
        assertThrows(IllegalArgumentException.class, () -> new Order(1, null));
    }

    @Test
    @DisplayName("Створення замовлення з невалідним номером викидає виключення")
    void testCreateOrderWithInvalidNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Order(0, "John"));
        assertThrows(IllegalArgumentException.class, () -> new Order(-1, "John"));
    }

    @Test
    @DisplayName("Ім'я замовлення обрізається від пробілів")
    void testOrderNameIsTrimmed() {
        Order order = new Order(1, "  John Doe  ");
        assertEquals("John Doe", order.getName());
    }

    @Test
    @DisplayName("Два замовлення з однаковими даними рівні")
    void testOrdersWithSameDataAreEqual() {
        Order order1 = new Order(1, "John");
        Order order2 = new Order(1, "John");

        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
    }

    @Test
    @DisplayName("Два замовлення з різними даними не рівні")
    void testOrdersWithDifferentDataAreNotEqual() {
        Order order1 = new Order(1, "John");
        Order order2 = new Order(2, "John");
        Order order3 = new Order(1, "Jane");

        assertNotEquals(order1, order2);
        assertNotEquals(order1, order3);
    }

    @Test
    @DisplayName("toString повертає читабельний рядок")
    void testToStringReturnsReadableString() {
        Order order = new Order(42, "John Doe");
        String result = order.toString();

        assertTrue(result.contains("42"));
        assertTrue(result.contains("John Doe"));
    }
}

