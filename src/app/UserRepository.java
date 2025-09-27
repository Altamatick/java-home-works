package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private final List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
        users.add(new User(1, "Alice Smith", "alice@example.com"));
        users.add(new User(2, "Bob Johnson", "bob@example.com"));
        users.add(new User(3, "Charlie Brown", "charlie@example.com"));
        users.add(new User(4, "Diana Prince", "diana@example.com"));
        users.add(new User(5, "Eve Wilson", "eve@example.com"));
    }

    public Optional<User> findUserById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    public Optional<User> findUserByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public Optional<List<User>> findAllUsers() {
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new ArrayList<>(users));
    }
}
