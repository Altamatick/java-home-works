package app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public void createUsersTable() throws SQLException {
        String createTableSQL =
                "CREATE TABLE IF NOT EXISTS users (" +
                        "id SERIAL PRIMARY KEY, " +
                        "name VARCHAR(255) NOT NULL, " +
                        "age INTEGER, " +
                        "email VARCHAR(255) NOT NULL" +
                ")";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Таблиця users успішно створена!");
        }
    }

    public void insertSampleData() throws SQLException {
        String[] insertStatements = {
            "INSERT INTO users (name, age, email) VALUES ('John', 30, 'john@example.com')",
            "INSERT INTO users (name, age, email) VALUES ('Alice', 25, 'alice@example.com')",
            "INSERT INTO users (name, age, email) VALUES ('Bob', 35, 'bob@example.com')"
        };

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            for (String insertSQL : insertStatements) {
                statement.execute(insertSQL);
            }
            System.out.println("Тестові дані успішно вставлені в таблицю users!");
        }
    }

    public void selectAllUsers() throws SQLException {
        String selectSQL = "SELECT id, name, age, email FROM users";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            System.out.println("Всі користувачі з таблиці users:");
            System.out.println("ID | Ім'я  | Вік | Email");
            System.out.println("---|-------|-----|----------------");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String email = resultSet.getString("email");

                System.out.printf("%-3d| %-6s| %-3d | %s%n", id, name, age, email);
            }
        }
    }

    public void deleteUserByName(String userName) throws SQLException {
        String deleteSQL = "DELETE FROM users WHERE name = '" + userName + "'";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            int rowsAffected = statement.executeUpdate(deleteSQL);
            if (rowsAffected > 0) {
                System.out.println("Користувача " + userName + " успішно видалено з таблиці users!");
            } else {
                System.out.println("Користувача з ім'ям " + userName + " не знайдено в таблиці users.");
            }
        }
    }
}
