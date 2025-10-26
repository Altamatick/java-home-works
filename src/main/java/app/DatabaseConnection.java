package main.java.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/my_database";
    private static final String USERNAME = "myuser";
    private static final String PASSWORD = "mypassword";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
