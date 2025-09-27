package app;

import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        // Створюємо репозиторій користувачів
        UserRepository userRepository = new UserRepository();

        System.out.println("=== Демонстрація роботи з Optional у UserRepository ===\n");

        // 1. Пошук користувача за ID
        System.out.println("1. Пошук користувача за ID:");
        demonstrateSearchById(userRepository, 3); // існуючий користувач
        demonstrateSearchById(userRepository, 99); // неіснуючий користувач

        // 2. Пошук користувача за email
        System.out.println("\n2. Пошук користувача за email:");
        demonstrateSearchByEmail(userRepository, "alice@example.com"); // існуючий email
        demonstrateSearchByEmail(userRepository, "nonexistent@example.com"); // неіснуючий email

        // 3. Отримання списку всіх користувачів
        System.out.println("\n3. Отримання списку всіх користувачів:");
        demonstrateGetAllUsers(userRepository);

        // 4. Додаткові демонстрації Optional методів
        System.out.println("\n4. Додаткові методи Optional:");
        demonstrateOptionalMethods(userRepository);
    }

    // Демонстрація пошуку за ID
    private static void demonstrateSearchById(UserRepository repository, int id) {
        System.out.println("Шукаємо користувача з ID: " + id);
        Optional<User> userOptional = repository.findUserById(id);

        if (userOptional.isPresent()) {
            System.out.println("Знайдено: " + userOptional.get());
        } else {
            System.out.println("Користувача з ID " + id + " не знайдено");
        }
        System.out.println("---");
    }

    // Демонстрація пошуку за email
    private static void demonstrateSearchByEmail(UserRepository repository, String email) {
        System.out.println("Шукаємо користувача з email: " + email);
        Optional<User> userOptional = repository.findUserByEmail(email);

        userOptional.ifPresentOrElse(
            user -> System.out.println("Знайдено користувача: " + user),
            () -> System.out.println("Користувача з email " + email + " не знайдено")
        );

        // Використання map для отримання імені
        String userName = userOptional
            .map(User::getName)
            .orElse("Невідомий користувач");
        System.out.println("Ім'я користувача: " + userName);
        System.out.println("---");
    }

    // Демонстрація отримання всіх користувачів
    private static void demonstrateGetAllUsers(UserRepository repository) {
        Optional<List<User>> usersOptional = repository.findAllUsers();

        if (usersOptional.isPresent()) {
            List<User> users = usersOptional.get();
            System.out.println("Знайдено " + users.size() + " користувачів:");
            users.forEach(user -> System.out.println("  " + user));
        } else {
            System.out.println("Список користувачів порожній");
        }
    }

    // Додаткові демонстрації Optional методів
    private static void demonstrateOptionalMethods(UserRepository repository) {
        // filter + map + orElseThrow
        try {
            User adminUser = repository.findUserById(1)
                .filter(user -> user.getEmail().contains("alice"))
                .map(user -> new User(user.getId(), user.getName() + " (Admin)", user.getEmail()))
                .orElseThrow(() -> new RuntimeException("Admin користувач не знайдений"));

            System.out.println("Admin користувач: " + adminUser);
        } catch (RuntimeException e) {
            System.out.println("Помилка: " + e.getMessage());
        }

        // Ланцюжок Optional операцій
        String emailDomain = repository.findUserById(2)
            .map(User::getEmail)
            .map(email -> email.substring(email.indexOf("@") + 1))
            .orElse("domain.com");

        System.out.println("Email домен користувача ID=2: " + emailDomain);

        // flatMap приклад (якби у нас були вкладені Optional)
        Optional<String> userInfo = repository.findUserById(4).map(user -> user.getName() + " - " + user.getEmail());

        userInfo.ifPresent(info -> System.out.println("Інфо користувача: " + info));
    }
}
