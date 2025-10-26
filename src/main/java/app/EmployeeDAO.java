package app;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // Додавання нового співробітника
    public boolean addEmployee(Employee employee) throws SQLException {
        String insertSQL = "INSERT INTO employees (name, age, position, salary) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, employee.getName());
            preparedStatement.setInt(2, employee.getAge());
            preparedStatement.setString(3, employee.getPosition());
            preparedStatement.setFloat(4, employee.getSalary());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Отримання співробітника за ID
    public Employee getEmployeeById(int id) throws SQLException {
        String selectSQL = "SELECT id, name, age, position, salary FROM employees WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("position"),
                        resultSet.getFloat("salary")
                );
            }
        }
        return null;
    }

    // Отримання всіх співробітників
    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String selectSQL = "SELECT id, name, age, position, salary FROM employees ORDER BY id";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {

            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("position"),
                        resultSet.getFloat("salary")
                );
                employees.add(employee);
            }
        }
        return employees;
    }

    // Оновлення інформації про співробітника
    public boolean updateEmployee(Employee employee) throws SQLException {
        String updateSQL = "UPDATE employees SET name = ?, age = ?, position = ?, salary = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setString(1, employee.getName());
            preparedStatement.setInt(2, employee.getAge());
            preparedStatement.setString(3, employee.getPosition());
            preparedStatement.setFloat(4, employee.getSalary());
            preparedStatement.setInt(5, employee.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Видалення співробітника за ID
    public boolean deleteEmployee(int id) throws SQLException {
        String deleteSQL = "DELETE FROM employees WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Пошук співробітників за посадою
    public List<Employee> getEmployeesByPosition(String position) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String selectSQL = "SELECT id, name, age, position, salary FROM employees WHERE position = ? ORDER BY name";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setString(1, position);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("position"),
                        resultSet.getFloat("salary")
                );
                employees.add(employee);
            }
        }
        return employees;
    }

    // Отримання співробітників з зарплатою вище заданої суми
    public List<Employee> getEmployeesWithSalaryAbove(float minSalary) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String selectSQL = "SELECT id, name, age, position, salary FROM employees WHERE salary > ? ORDER BY salary DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {

            preparedStatement.setFloat(1, minSalary);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("position"),
                        resultSet.getFloat("salary")
                );
                employees.add(employee);
            }
        }
        return employees;
    }

    // Підрахунок загальної кількості співробітників
    public int getEmployeeCount() throws SQLException {
        String countSQL = "SELECT COUNT(*) as count FROM employees";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(countSQL)) {

            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        }
        return 0;
    }

    // Обчислення середньої зарплати
    public double getAverageSalary() throws SQLException {
        String avgSQL = "SELECT AVG(salary) as avg_salary FROM employees";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(avgSQL)) {

            if (resultSet.next()) {
                return resultSet.getDouble("avg_salary");
            }
        }
        return 0.0;
    }
}
