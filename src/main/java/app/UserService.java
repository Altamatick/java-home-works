package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    /**
     * Створює нового користувача та пости у транзакції
     * Якщо виникне помилка, відбувається rollback всіх змін
     */
    @Transactional
    public User createUserWithPosts(User user, List<Post> posts) {
        // Зберігаємо користувача
        User savedUser = userRepository.save(user);

        // Додаємо пости до користувача
        if (posts != null) {
            for (Post post : posts) {
                post.setUser(savedUser);
                savedUser.addPost(post);
            }
            // Зберігаємо пости
            postRepository.saveAll(posts);
        }

        return savedUser;
    }

    /**
     * Створює нового користувача
     */
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Отримання користувача по імені
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByName(String name) {
        return userRepository.findByName(name);
    }

    /**
     * Отримання всіх користувачів, у яких однаковий email domain
     * Наприклад: getUsersByEmailDomain("@gmail.com")
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByEmailDomain(String domain) {
        return userRepository.findByEmailEndingWith(domain);
    }

    /**
     * Отримання всіх постів по ідентифікатору користувача
     */
    @Transactional(readOnly = true)
    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUserId(userId);
    }

    /**
     * Метод для тестування rollback транзакції
     * Викидає виняток після створення користувача, щоб перевірити rollback
     */
    @Transactional
    public User createUserWithRollback(User user, boolean shouldThrowException) {
        User savedUser = userRepository.save(user);

        if (shouldThrowException) {
            throw new RuntimeException("Тестова помилка для перевірки rollback");
        }

        return savedUser;
    }

    /**
     * Отримання всіх користувачів
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Отримання користувача по id
     */
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
