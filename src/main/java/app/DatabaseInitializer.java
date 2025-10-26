package main.java.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseInitializer {
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
            System.out.println("Знайдено: " + foundEmployee.toString());
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
