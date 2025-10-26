package app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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

    public void createEmployeesTable() throws SQLException {
        String createTableSQL =
                "CREATE TABLE IF NOT EXISTS employees (" +
                        "id SERIAL PRIMARY KEY, " +
                        "name VARCHAR(255) NOT NULL, " +
                        "age INTEGER NOT NULL, " +
                        "position VARCHAR(255) NOT NULL, " +
                        "salary REAL NOT NULL" +
                        ")";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Таблиця employees успішно створена!");
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

    // Демонстрація роботи з EmployeeDAO
    public void demonstrateEmployeeDAO() throws SQLException {
        EmployeeDAO employeeDAO = new EmployeeDAO();

        System.out.println("\n=== Демонстрація роботи з EmployeeDAO ===");

        // 1. Додавання нових співробітників
        System.out.println("\n1. Додавання співробітників:");
        Employee emp1 = new Employee("Іван Петров", 28, "Розробник", 45000.0f);
        Employee emp2 = new Employee("Марія Іваненко", 32, "Менеджер", 55000.0f);
        Employee emp3 = new Employee("Олександр Коваленко", 25, "Дизайнер", 35000.0f);
        Employee emp4 = new Employee("Анна Сидоренко", 30, "Розробник", 48000.0f);

        if (employeeDAO.addEmployee(emp1)) {
            System.out.println("✓ Додано: " + emp1.getName());
        }
        if (employeeDAO.addEmployee(emp2)) {
            System.out.println("✓ Додано: " + emp2.getName());
        }
        if (employeeDAO.addEmployee(emp3)) {
            System.out.println("✓ Додано: " + emp3.getName());
        }
        if (employeeDAO.addEmployee(emp4)) {
            System.out.println("✓ Додано: " + emp4.getName());
        }

        // 2. Отримання всіх співробітників
        System.out.println("\n2. Список всіх співробітників:");
        List<Employee> allEmployees = employeeDAO.getAllEmployees();
        for (Employee emp : allEmployees) {
            System.out.printf("ID: %d | %s | Вік: %d | Посада: %s | Зарплата: %.2f грн%n",
                    emp.getId(), emp.getName(), emp.getAge(), emp.getPosition(), emp.getSalary());
        }

        // 3. Пошук співробітника за ID
        System.out.println("\n3. Пошук співробітника за ID (ID = 1):");
        Employee foundEmployee = employeeDAO.getEmployeeById(1);
        if (foundEmployee != null) {
            System.out.println("Знайдено: " + foundEmployee);
        }

        // 4. Оновлення інформації про співробітника
        System.out.println("\n4. Оновлення зарплати співробітника:");
        if (foundEmployee != null) {
            foundEmployee.setSalary(50000.0f);
            if (employeeDAO.updateEmployee(foundEmployee)) {
                System.out.println("✓ Зарплата оновлена для: " + foundEmployee.getName());
            }
        }

        // 5. Пошук за посадою
        System.out.println("\n5. Співробітники на посаді 'Розробник':");
        List<Employee> developers = employeeDAO.getEmployeesByPosition("Розробник");
        for (Employee dev : developers) {
            System.out.printf("• %s - %.2f грн%n", dev.getName(), dev.getSalary());
        }

        // 6. Співробітники з високою зарплатою
        System.out.println("\n6. Співробітники з зарплатою вище 40000 грн:");
        List<Employee> highSalaryEmployees = employeeDAO.getEmployeesWithSalaryAbove(40000.0f);
        for (Employee emp : highSalaryEmployees) {
            System.out.printf("• %s - %.2f грн%n", emp.getName(), emp.getSalary());
        }

        // 7. Статистика
        System.out.println("\n7. Статистика:");
        int totalEmployees = employeeDAO.getEmployeeCount();
        double avgSalary = employeeDAO.getAverageSalary();
        System.out.printf("Загальна кількість співробітників: %d%n", totalEmployees);
        System.out.printf("Середня зарплата: %.2f грн%n", avgSalary);

        // 8. Демонстрація видалення
        System.out.println("\n8. Видалення співробітника (ID = 3):");
        if (employeeDAO.deleteEmployee(3)) {
            System.out.println("✓ Співробітника видалено");
        }

        // Перевірка після видалення
        System.out.println("\nСписок після видалення:");
        allEmployees = employeeDAO.getAllEmployees();
        for (Employee emp : allEmployees) {
            System.out.printf("ID: %d | %s | %s%n",
                    emp.getId(), emp.getName(), emp.getPosition());
        }
    }
}
