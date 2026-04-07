package dev.sorokin.eventmanager.users;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(SingUpRequest singUpRequest) {

        if (userRepository.existsByLogin(singUpRequest.login())) {
            throw new IllegalArgumentException("Username already exists");
        }
        var userToSave = new UserEntity(
                null,
                singUpRequest.login(),
                singUpRequest.password(),
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
