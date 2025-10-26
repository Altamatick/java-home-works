package coffee.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Демонстрація роботи системи з прикладу з технічного завдання.
 */
public class CoffeeShopExampleDemo {
    private static final Logger logger = LoggerFactory.getLogger(CoffeeShopExampleDemo.class);

    public static void main(String[] args) {
        logger.info("=== Демонстрація прикладу з ТЗ ===");

        CoffeeOrderBoard board = new CoffeeOrderBoard();

        // Симулюємо роботу кав'ярні, де вже було кілька замовлень
        // Додаємо замовлення, щоб отримати номери як у прикладі: 4, 27, 33, 34

        // Додаємо перші 3 замовлення та видаємо їх (щоб досягти номера 4)
        logger.info("Симуляція попередніх замовлень...");
        for (int i = 1; i <= 3; i++) {
            board.add("Customer " + i);
            board.deliver();
        }

        // Додаємо замовлення №4
        board.add("Alen");

        // Додаємо і видаємо замовлення з 5 по 26
        for (int i = 5; i <= 26; i++) {
            board.add("Customer " + i);
            board.deliver();
        }

        // Додаємо замовлення №27
        board.add("Yoda");

        // Додаємо і видаємо замовлення з 28 по 32
        for (int i = 28; i <= 32; i++) {
            board.add("Customer " + i);
            board.deliver();
        }

        // Додаємо замовлення №33 та №34
        board.add("Obi-van");
        board.add("John Snow");

        System.out.println("\n=== Поточний стан черги (як у прикладі з ТЗ) ===");
        board.draw();

        // Демонстрація видачі найближчого замовлення
        System.out.println("\n=== Видача найближчого замовлення ===");
        Optional<Order> delivered = board.deliver();
        delivered.ifPresent(o ->
            System.out.println("Видано: № " + o.getNumber() + " - " + o.getName())
        );

        System.out.println("\n=== Стан черги після видачі ===");
        board.draw();

        // Демонстрація видачі конкретного замовлення (№33)
        System.out.println("\n=== Видача замовлення №33 поза чергою ===");
        Optional<Order> deliveredSpecific = board.deliver(33);
        deliveredSpecific.ifPresent(o ->
            System.out.println("Видано поза чергою: № " + o.getNumber() + " - " + o.getName())
        );

        System.out.println("\n=== Фінальний стан черги ===");
        board.draw();

        logger.info("=== Демонстрація завершена ===");
    }
}

