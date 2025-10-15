package app;

import java.sql.Connection;
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
}
