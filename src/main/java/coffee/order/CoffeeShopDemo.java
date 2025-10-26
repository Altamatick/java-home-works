package coffee.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Демонстраційний клас для тестування системи замовлень кави.
 */
public class CoffeeShopDemo {
    private static final Logger logger = LoggerFactory.getLogger(CoffeeShopDemo.class);

    public static void main(String[] args) {
        logger.info("=== Запуск демонстрації роботи кав'ярні ===");

        try {
            CoffeeOrderBoard board = new CoffeeOrderBoard();

            // Демонстрація додавання замовлень
            demonstrateAddingOrders(board);

            // Демонстрація виведення черги
            demonstrateDrawBoard(board);

            // Демонстрація видачі замовлення за чергою
            demonstrateDeliverNextOrder(board);

            // Демонстрація виведення оновленої черги
            demonstrateDrawBoard(board);

            // Демонстрація видачі конкретного замовлення
            demonstrateDeliverSpecificOrder(board, 27);

            // Демонстрація виведення оновленої черги
            demonstrateDrawBoard(board);

            // Додаємо ще замовлення для демонстрації натурального порядку
            demonstrateNaturalOrdering(board);

            // Демонстрація обробки помилок
            demonstrateErrorHandling(board);

            // Фінальний стан черги
            System.out.println("\n=== Фінальний стан черги ===");
            board.draw();

            logger.info("=== Демонстрація завершена успішно ===");

        } catch (Exception e) {
            logger.error("Критична помилка під час демонстрації", e);
        }
    }

    private static void demonstrateAddingOrders(CoffeeOrderBoard board) {
        System.out.println("\n=== Додавання замовлень ===");
        logger.info("Початок додавання замовлень");

        board.add("Alen");
        board.add("Yoda");
        board.add("Obi-van");
        board.add("John Snow");

        System.out.println("Додано 4 замовлення");
    }

    private static void demonstrateDrawBoard(CoffeeOrderBoard board) {
        System.out.println("\n=== Поточний стан черги ===");
        board.draw();
    }

    private static void demonstrateDeliverNextOrder(CoffeeOrderBoard board) {
        System.out.println("\n=== Видача найближчого замовлення ===");
        logger.info("Видача замовлення за чергою");

        Optional<Order> order = board.deliver();
        order.ifPresent(o -> System.out.println("Видано замовлення: № " + o.getNumber() + " - " + o.getName()));
    }

    private static void demonstrateDeliverSpecificOrder(CoffeeOrderBoard board, int orderNumber) {
        System.out.println("\n=== Видача конкретного замовлення ==");
        logger.info("Видача замовлення з номером {}", orderNumber);

        Optional<Order> order = board.deliver(orderNumber);
        if (order.isPresent()) {
            System.out.println("Видано замовлення поза чергою: № " + order.get().getNumber() + " - " + order.get().getName());
        } else {
            System.out.println("Замовлення № " + orderNumber + " не знайдено");
        }
    }

    private static void demonstrateNaturalOrdering(CoffeeOrderBoard board) {
        System.out.println("\n=== Демонстрація натурального порядку ===");
        logger.info("Додавання нових замовлень для демонстрації натурального порядку");

        int lastNumber = board.getLastOrderNumber();
        System.out.println("Останній номер замовлення: " + lastNumber);

        Order newOrder = board.add("Luke Skywalker");
        System.out.println("Нове замовлення отримало номер: " + newOrder.getNumber() + " (очікувалось " + (lastNumber + 1) + ")");
    }

    private static void demonstrateErrorHandling(CoffeeOrderBoard board) {
        System.out.println("\n=== Демонстрація обробки помилок ===");
        logger.info("Тестування обробки помилок");

        // Спроба додати замовлення з порожнім ім'ям
        try {
            board.add("");
        } catch (IllegalArgumentException e) {
            System.out.println("Перехоплено помилку: " + e.getMessage());
        }

        // Спроба додати замовлення з null
        try {
            board.add(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Перехоплено помилку: " + e.getMessage());
        }

        // Спроба видати неіснуюче замовлення
        System.out.println("\nСпроба видати неіснуюче замовлення №999:");
        Optional<Order> order = board.deliver(999);
        if (order.isEmpty()) {
            System.out.println("Замовлення не знайдено (очікувана поведінка)");
        }

        // Створимо нову порожню чергу для тесту
        CoffeeOrderBoard emptyBoard = new CoffeeOrderBoard();
        System.out.println("\nСпроба видати замовлення з порожньої черги:");
        Optional<Order> emptyOrder = emptyBoard.deliver();
        if (emptyOrder.isEmpty()) {
            System.out.println("Черга порожня (очікувана поведінка)");
        }
    }
}

