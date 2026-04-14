package dev.sorokin.eventmanager.users;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (userService.existByLogin("new-user")) {
            return;
        }

        userService.createUser(
                "new-user",
                passwordEncoder.encode("12345"),
                21,
                UserRole.USER
        );
    }
}
