package coffee.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Клас, що представляє чергу замовлень у кав'ярні.
 * Управляє списком замовлень: додавання, видача та відображення.
 */
public class CoffeeOrderBoard {
    private static final Logger logger = LoggerFactory.getLogger(CoffeeOrderBoard.class);

    private final List<Order> orders;
    private int lastOrderNumber;

    /**
     * Конструктор для створення порожньої черги замовлень.
     */
    public CoffeeOrderBoard() {
        this.orders = new ArrayList<>();
        this.lastOrderNumber = 0;
        logger.info("Створено нову чергу замовлень");
    }

    /**
     * Додає нове замовлення до черги.
     * Номер замовлення присвоюється автоматично в натуральному порядку.
     *
     * @param name ім'я замовника
     * @return створене замовлення
     */
    public Order add(String name) {
        logger.debug("Спроба додати замовлення для {}", name);

        try {
            lastOrderNumber++;
            Order order = new Order(lastOrderNumber, name);
            orders.add(order);
            logger.info("Додано замовлення: номер {}, замовник '{}'", order.getNumber(), order.getName());
            return order;
        } catch (IllegalArgumentException e) {
            lastOrderNumber--; // відкат номера у разі помилки
            logger.error("Помилка при додаванні замовлення для '{}'", name, e);
            throw e;
        }
    }

    /**
     * Видає найближче на черзі замовлення (перше в списку).
     * Замовлення видаляється зі списку після видачі.
     *
     * @return видане замовлення або Optional.empty() якщо черга порожня
     */
    public Optional<Order> deliver() {
        logger.debug("Спроба видати найближче замовлення");

        if (orders.isEmpty()) {
            logger.warn("Спроба видати замовлення з порожньої черги");
            return Optional.empty();
        }

        Order order = orders.remove(0);
        logger.info("Видано замовлення: номер {}, замовник '{}'", order.getNumber(), order.getName());
        return Optional.of(order);
    }

    /**
     * Видає замовлення з певним номером.
     * Замовлення видаляється зі списку після видачі.
     * Обробляє ситуацію, коли замовлення готове раніше, ніж інші.
     *
     * @param orderNumber номер замовлення для видачі
     * @return видане замовлення або Optional.empty() якщо замовлення не знайдено
     */
    public Optional<Order> deliver(int orderNumber) {
        logger.debug("Спроба видати замовлення з номером {}", orderNumber);

        Optional<Order> orderOpt = orders.stream()
                .filter(order -> order.getNumber() == orderNumber)
                .findFirst();

        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            orders.remove(order);
            logger.info("Видано замовлення поза чергою: номер {}, замовник '{}'", order.getNumber(), order.getName());
            return Optional.of(order);
        } else {
            logger.warn("Замовлення з номером {} не знайдено у черзі", orderNumber);
            return Optional.empty();
        }
    }

    /**
     * Виводить у консоль інформацію про поточний стан черги.
     * Замовлення відсортовані у порядку найближчого до видачі.
     */
    public void draw() {
        logger.debug("Виведення поточного стану черги");

        if (orders.isEmpty()) {
            System.out.println("Черга замовлень порожня");
            logger.info("Черга замовлень порожня при виклику draw()");
            return;
        }

        System.out.println("======================");
        System.out.printf("%-6s | %s%n", "Num", "Name");
        System.out.println("----------------------");

        for (Order order : orders) {
            System.out.printf("%-6d | %s%n", order.getNumber(), order.getName());
        }

        System.out.println("======================");
        logger.info("Виведено стан черги: {} замовлень", orders.size());
    }

    /**
     * Отримати кількість замовлень у черзі.
     *
     * @return кількість замовлень
     */
    public int getOrdersCount() {
        return orders.size();
    }

    /**
     * Отримати номер останнього створеного замовлення.
     *
     * @return номер останнього замовлення
     */
    public int getLastOrderNumber() {
        return lastOrderNumber;
    }

    /**
     * Очистити всю чергу замовлень.
     */
    public void clear() {
        int count = orders.size();
        orders.clear();
        logger.info("Очищено чергу, видалено {} замовлень", count);
    }
}

