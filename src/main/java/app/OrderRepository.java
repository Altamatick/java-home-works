package app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository для роботи з замовленнями.
 * Spring Data JPA автоматично надає реалізацію методів:
 * - findById(Long id) - отримання замовлення за id
 * - findAll() - отримання всіх замовлень
 * - save(Order order) - додавання/оновлення замовлення
 * - deleteById(Long id) - видалення замовлення
 * - existsById(Long id) - перевірка існування замовлення
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
