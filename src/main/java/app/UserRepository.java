package app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository для роботи з користувачами.
 * Spring Data JPA автоматично надає методи:
 * - findById(Long id)
 * - findAll()
 * - save(User user)
 * - deleteById(Long id)
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Знаходить користувачів за їхнім ім'ям
     * Spring Data JPA автоматично створює запит на основі імені методу
     */
    List<User> findByName(String name);

    /**
     * Знаходить користувачів, чиї адреси електронної пошти закінчуються на певний домен
     * Наприклад: findByEmailEndingWith("@gmail.com")
     */
    List<User> findByEmailEndingWith(String domain);
}
