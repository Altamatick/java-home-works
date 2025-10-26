package coffee.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для класу CoffeeOrderBoard.
 */
@DisplayName("Тести черги замовлень кав'ярні")
class CoffeeOrderBoardTest {

    private CoffeeOrderBoard board;

    @BeforeEach
    void setUp() {
        board = new CoffeeOrderBoard();
    }

    @Test
    @DisplayName("Додавання замовлення збільшує номер у натуральному порядку")
    void testAddOrderIncrementsOrderNumber() {
        Order order1 = board.add("John");
        Order order2 = board.add("Jane");
        Order order3 = board.add("Bob");

        assertEquals(1, order1.getNumber());
        assertEquals(2, order2.getNumber());
        assertEquals(3, order3.getNumber());
    }

    @Test
    @DisplayName("Додавання замовлення з порожнім ім'ям викидає виключення")
    void testAddOrderWithEmptyNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> board.add(""));
        assertThrows(IllegalArgumentException.class, () -> board.add("   "));
        assertThrows(IllegalArgumentException.class, () -> board.add(null));
    }

    @Test
    @DisplayName("Видача найближчого замовлення повертає перше в черзі")
    void testDeliverReturnsFirstOrder() {
        board.add("John");
        board.add("Jane");
        board.add("Bob");

        Optional<Order> delivered = board.deliver();

        assertTrue(delivered.isPresent());
        assertEquals(1, delivered.get().getNumber());
        assertEquals("John", delivered.get().getName());
        assertEquals(2, board.getOrdersCount());
    }

    @Test
    @DisplayName("Видача з порожньої черги повертає Optional.empty()")
    void testDeliverFromEmptyBoardReturnsEmpty() {
        Optional<Order> delivered = board.deliver();
        assertTrue(delivered.isEmpty());
    }

    @Test
    @DisplayName("Видача конкретного замовлення видаляє його з черги")
    void testDeliverSpecificOrderRemovesIt() {
        board.add("John");
        board.add("Jane");
        board.add("Bob");

        Optional<Order> delivered = board.deliver(2);

        assertTrue(delivered.isPresent());
        assertEquals(2, delivered.get().getNumber());
        assertEquals("Jane", delivered.get().getName());
        assertEquals(2, board.getOrdersCount());
    }

    @Test
    @DisplayName("Видача неіснуючого замовлення повертає Optional.empty()")
    void testDeliverNonExistentOrderReturnsEmpty() {
        board.add("John");

        Optional<Order> delivered = board.deliver(999);

        assertTrue(delivered.isEmpty());
        assertEquals(1, board.getOrdersCount());
    }

    @Test
    @DisplayName("Натуральний порядок номерів зберігається після видалення")
    void testNaturalOrderingAfterDelivery() {
        board.add("John");
        board.add("Jane");
        board.deliver();
        Order order3 = board.add("Bob");

        assertEquals(3, order3.getNumber());
        assertEquals(3, board.getLastOrderNumber());
    }

    @Test
    @DisplayName("Очищення черги видаляє всі замовлення")
    void testClearRemovesAllOrders() {
        board.add("John");
        board.add("Jane");
        board.add("Bob");

        assertEquals(3, board.getOrdersCount());

        board.clear();

        assertEquals(0, board.getOrdersCount());
    }

    @Test
    @DisplayName("Номери замовлень не повторюються")
    void testOrderNumbersAreUnique() {
        Order order1 = board.add("John");
        Order order2 = board.add("Jane");
        board.deliver(1); // Видаємо перше замовлення
        Order order3 = board.add("Bob");

        assertEquals(1, order1.getNumber());
        assertEquals(2, order2.getNumber());
        assertEquals(3, order3.getNumber());

        // Перевіряємо, що немає дублікатів
        assertNotEquals(order1.getNumber(), order2.getNumber());
        assertNotEquals(order1.getNumber(), order3.getNumber());
        assertNotEquals(order2.getNumber(), order3.getNumber());
    }

    @Test
    @DisplayName("Видача замовлення поза чергою (як у ТЗ)")
    void testDeliverOutOfOrder() {
        // Симулюємо ситуацію з ТЗ: замовлення прийшло пізніше, але готове раніше
        board.add("First");
        board.add("Second");
        board.add("Third");

        // Видаємо друге замовлення поза чергою
        Optional<Order> delivered = board.deliver(2);

        assertTrue(delivered.isPresent());
        assertEquals(2, delivered.get().getNumber());
        assertEquals("Second", delivered.get().getName());

        // Перевіряємо, що в черзі залишилися 1 та 3
        assertEquals(2, board.getOrdersCount());
    }
}

