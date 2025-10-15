package app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public void createSalesTable() throws SQLException {
        String createTableSQL =
                "CREATE TABLE IF NOT EXISTS sales (" +
                        "id SERIAL PRIMARY KEY, " +
                        "product VARCHAR(255) NOT NULL, " +
                        "price DECIMAL(10,2) NOT NULL, " +
                        "quantity INTEGER NOT NULL" +
                ")";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Таблиця sales успішно створена!");
        }
    }

    public void insertSalesData() throws SQLException {
        String[] insertStatements = {
            "INSERT INTO sales (product, price, quantity) VALUES ('Laptop', 1000, 5)",
            "INSERT INTO sales (product, price, quantity) VALUES ('Phone', 700, 3)",
            "INSERT INTO sales (product, price, quantity) VALUES ('Tablet', 500, 2)",
            "INSERT INTO sales (product, price, quantity) VALUES ('Printer', 300, 4)"
        };

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            for (String insertSQL : insertStatements) {
                statement.execute(insertSQL);
            }
            System.out.println("Дані продажів успішно вставлені в таблицю sales!");
        }
    }

    public void selectAllSales() throws SQLException {
        String selectSQL = "SELECT id, product, price, quantity FROM sales";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            System.out.println("\n=== Всі записи з таблиці sales ===");
            System.out.println("ID | Продукт  | Ціна    | Кількість");
            System.out.println("---|----------|---------|----------");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String product = resultSet.getString("product");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");

                System.out.printf("%-3d| %-9s| %-8.2f| %-9d%n", id, product, price, quantity);
            }
        }
    }

    public void selectLimitedSales() throws SQLException {
        String selectSQL = "SELECT id, product, price, quantity FROM sales LIMIT 2";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            System.out.println("\n=== Перші два записи з таблиці sales ===");
            System.out.println("ID | Продукт  | Ціна    | Кількість");
            System.out.println("---|----------|---------|----------");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String product = resultSet.getString("product");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");

                System.out.printf("%-3d| %-9s| %-8.2f| %-9d%n", id, product, price, quantity);
            }
        }
    }

    public void calculateTotalValue() throws SQLException {
        String sumSQL = "SELECT SUM(price * quantity) as total_value FROM sales";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sumSQL)) {

            System.out.println("\n=== Загальна вартість всіх продуктів ===");

            if (resultSet.next()) {
                double totalValue = resultSet.getDouble("total_value");
                System.out.printf("Загальна вартість: %.2f грн%n", totalValue);
            }
        }
    }

    public void groupByProduct() throws SQLException {
        String groupSQL =
                "SELECT product, " +
                       "SUM(quantity) as total_quantity, " +
                       "AVG(price) as average_price " +
                "FROM sales " +
                "GROUP BY product " +
                "ORDER BY product";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(groupSQL)) {

            System.out.println("\n=== Дані згруповані за продуктами ===");
            System.out.println("Продукт   | Загальна кількість | Середня ціна");
            System.out.println("----------|-------------------|-------------");

            while (resultSet.next()) {
                String product = resultSet.getString("product");
                int totalQuantity = resultSet.getInt("total_quantity");
                double averagePrice = resultSet.getDouble("average_price");

                System.out.printf("%-10s| %-18d| %-12.2f%n", product, totalQuantity, averagePrice);
            }
        }
    }
}
