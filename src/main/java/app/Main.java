package app;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer dbInitializer = new DatabaseInitializer();

        try {
            // 1. Створення таблиці sales
            dbInitializer.createSalesTable();

            // 2. Вставка даних про продукти
            dbInitializer.insertSalesData();

            // 3. Вибірка всіх записів
            dbInitializer.selectAllSales();

            // 4. Вибірка з обмеженням (LIMIT 2)
            dbInitializer.selectLimitedSales();

            // 5. Обчислення загальної вартості (SUM)
            dbInitializer.calculateTotalValue();

            // 6. Групування даних (GROUP BY)
            dbInitializer.groupByProduct();

        } catch (SQLException e) {
            System.err.println("Помилка при роботі з базою даних: " + e.getMessage());
            e.printStackTrace();
        }
    }
}