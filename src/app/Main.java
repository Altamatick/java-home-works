package app;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer dbInitializer = new DatabaseInitializer();

        try {
            dbInitializer.createUsersTable();
            dbInitializer.insertSampleData();
        } catch (SQLException e) {
            System.err.println("Помилка при роботі з базою даних: " + e.getMessage());
            e.printStackTrace();
        }
    }
}