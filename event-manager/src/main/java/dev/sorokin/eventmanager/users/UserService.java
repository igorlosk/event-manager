package dev.sorokin.eventmanager.users;

import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(SingUpRequest singUpRequest) {

        if (userRepository.existsByLogin(singUpRequest.login())) {
            throw new IllegalArgumentException("Username already exists");
        }
        var hashedPass = passwordEncoder.encode(singUpRequest.password());
        var userToSave = new UserEntity(
                null,
                singUpRequest.login(),
                hashedPass,
                singUpRequest.age(),
                UserRole.USER
        );
        var savedUser = userRepository.save(userToSave);

        return new User(
                savedUser.getId(),
                savedUser.getLogin(),
                savedUser.getPasswordHash(),
                savedUser.getAge(),
                UserRole.valueOf(savedUser.getRole().toString())
        );
    }
}
