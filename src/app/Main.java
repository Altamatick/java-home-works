package app;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Демонстрація паттерну Singleton для Logger ===\n");

        // Тест 1: Отримання екземпляру Logger з різних частин програми
        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();
        Logger logger3 = Logger.getInstance();

        // Перевірка, що всі посилання вказують на один і той же об'єкт
        System.out.println("Перевірка Singleton:");
        System.out.println("logger1 == logger2: " + (logger1 == logger2));
        System.out.println("logger2 == logger3: " + (logger2 == logger3));
        System.out.println("logger1 == logger3: " + (logger1 == logger3));
        System.out.println("Хеш-код logger1: " + logger1.hashCode());
        System.out.println("Хеш-код logger2: " + logger2.hashCode());
        System.out.println("Хеш-код logger3: " + logger3.hashCode());
        System.out.println();

        // Тест 2: Логування з різних "частин" програми
        simulateApplicationStart(logger1);
        simulateUserAction(logger2);
        simulateDataProcessing(logger3);
        simulateApplicationEnd();

        // Тест 3: Перевірка, що всі логи зберігаються в одному місці
        Logger finalLogger = Logger.getInstance();
        System.out.println("Загальна кількість логів: " + finalLogger.getLogCount());
        finalLogger.printAllLogs();
    }

    private static void simulateApplicationStart(Logger logger) {
        System.out.println("--- Симуляція запуску програми ---");
        logger.log("Програма запущена");
        logger.log("Ініціалізація компонентів завершена");
    }

    private static void simulateUserAction(Logger logger) {
        System.out.println("\n--- Симуляція дій користувача ---");
        logger.log("Користувач увійшов в систему");
        logger.log("Користувач відкрив головне меню");
        logger.log("Користувач обрав опцію 'Налаштування'");
    }

    private static void simulateDataProcessing(Logger logger) {
        System.out.println("\n--- Симуляція обробки даних ---");
        logger.log("Початок обробки файлу data.txt");
        logger.log("Оброблено 100 записів");
        logger.log("Обробка файлу завершена успішно");
    }

    private static void simulateApplicationEnd() {
        System.out.println("\n--- Симуляція завершення програми ---");
        Logger logger = Logger.getInstance();
        logger.log("Збереження налаштувань");
        logger.log("Закриття з'єднань з базою даних");
        logger.log("Програма завершена");
    }
}