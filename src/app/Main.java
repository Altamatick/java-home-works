package app;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer dbInitializer = new DatabaseInitializer();

        try {
            dbInitializer.createUsersTable();
            dbInitializer.insertSampleData();

            System.out.println("\n=== Вибірка всіх користувачів ===");
            dbInitializer.selectAllUsers();

            System.out.println("\n=== Видалення користувача Bob ===");
            dbInitializer.deleteUserByName("Bob");

            System.out.println("\n=== Вибірка після видалення ===");
            dbInitializer.selectAllUsers();

        } catch (SQLException e) {
            System.err.println("Помилка при роботі з базою даних: " + e.getMessage());
            e.printStackTrace();
        }
    }
}