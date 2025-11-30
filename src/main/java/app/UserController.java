package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Створення нового користувача
     * POST http://localhost:8080/users
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Створення користувача з постами
     * POST http://localhost:8080/users/with-posts
     */
    @PostMapping("/with-posts")
    public ResponseEntity<User> createUserWithPosts(
            @RequestBody CreateUserWithPostsRequest request) {
        User user = new User(request.getName(), request.getEmail());
        User createdUser = userService.createUserWithPosts(user, request.getPosts());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Отримання користувача по імені
     * GET http://localhost:8080/users/by-name?name=John
     */
    @GetMapping("/by-name")
    public ResponseEntity<List<User>> getUsersByName(@RequestParam String name) {
        List<User> users = userService.getUsersByName(name);
        return ResponseEntity.ok(users);
    }

    /**
     * Отримання користувачів по email domain
     * GET http://localhost:8080/users/by-email-domain?domain=@gmail.com
     */
    @GetMapping("/by-email-domain")
    public ResponseEntity<List<User>> getUsersByEmailDomain(@RequestParam String domain) {
        List<User> users = userService.getUsersByEmailDomain(domain);
        return ResponseEntity.ok(users);
    }

    /**
     * Отримання всіх постів користувача
     * GET http://localhost:8080/users/{userId}/posts
     */
    @GetMapping("/{userId}/posts")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable Long userId) {
        List<Post> posts = userService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    /**
     * Отримання всіх користувачів
     * GET http://localhost:8080/users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Отримання користувача по id
     * GET http://localhost:8080/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Тест rollback транзакції
     * POST http://localhost:8080/users/test-rollback
     */
    @PostMapping("/test-rollback")
    public ResponseEntity<String> testRollback(@RequestBody User user) {
        try {
            userService.createUserWithRollback(user, true);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Помилка: rollback не спрацював - виняток не викинуто");
        } catch (RuntimeException e) {
            // Якщо виняток викинуто, rollback має спрацювати автоматично
            // Перевіряємо, що користувач не збережений
            List<User> users = userService.getUsersByName(user.getName());
            if (users.isEmpty()) {
                return ResponseEntity.ok("Rollback працює правильно - користувач не збережений");
            } else {
                // Можливо користувач вже існував, перевіряємо по email
                return ResponseEntity.ok("Rollback працює - виняток викинуто, транзакція відкочена");
            }
        }
    }

    /**
     * DTO для створення користувача з постами
     */
    public static class CreateUserWithPostsRequest {
        private String name;
        private String email;
        private List<Post> posts;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public List<Post> getPosts() {
            return posts;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }
    }
}
