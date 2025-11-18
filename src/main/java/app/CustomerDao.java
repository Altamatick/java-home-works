package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Customer> rowMapper;

    @Autowired
    public CustomerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new CustomerRowMapper();
    }

    /**
     * Додавання нового клієнта
     */
    public Customer add(Customer customer) {
        String sql = "INSERT INTO customer (full_name, email, social_security_number) VALUES (?, ?, ?) RETURNING id";
        Long id = jdbcTemplate.queryForObject(sql, Long.class,
                customer.getFullName(),
                customer.getEmail(),
                customer.getSocialSecurityNumber());
        customer.setId(id);
        return customer;
    }

    /**
     * Пошук клієнта по id
     */
    public Optional<Customer> findById(Long id) {
        String sql = "SELECT id, full_name, email, social_security_number FROM customer WHERE id = ?";
        try {
            Customer customer = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(customer);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * Оновлення клієнта
     */
    public boolean update(Customer customer) {
        String sql = "UPDATE customer SET full_name = ?, email = ?, social_security_number = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                customer.getFullName(),
                customer.getEmail(),
                customer.getSocialSecurityNumber(),
                customer.getId());
        return rowsAffected > 0;
    }

    /**
     * Видалення клієнта по id
     */
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM customer WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }

    /**
     * Отримання всіх клієнтів
     */
    public List<Customer> findAll() {
        String sql = "SELECT id, full_name, email, social_security_number FROM customer ORDER BY id";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
