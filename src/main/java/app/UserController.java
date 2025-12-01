package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String usersList(Model model, Authentication authentication) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        if (authentication != null) {
            model.addAttribute("currentUser", authentication.getName());
        }
        return "users";
    }
}
