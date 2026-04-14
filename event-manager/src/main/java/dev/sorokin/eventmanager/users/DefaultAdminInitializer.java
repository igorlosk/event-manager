package dev.sorokin.eventmanager.users;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultAdminInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public DefaultAdminInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (userService.existByLogin("admin")) {
            return;
        }

        userService.createUser(
                "admin",
                passwordEncoder.encode("admin"),
                30,
                UserRole.ADMIN
        );
    }
}
