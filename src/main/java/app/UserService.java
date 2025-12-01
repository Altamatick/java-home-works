package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String name, String email, String phone, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Користувач з таким email вже існує");
        }

        User user = new User(name, email, phone, passwordEncoder.encode(password));

        // Додати роль USER за замовчуванням
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Роль USER не знайдена"));
        user.addRole(userRole);

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Користувач не знайдений"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
