package dev.sorokin.eventmanager.users;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
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

        return mapToDomain(savedUser);
    }

    public User findByLogin(String login) {

        var user = userRepository.findByLogin(login)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));

        return mapToDomain(user);
    }

    public User findById(Long id){
        var user = userRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("User not found"));

        return mapToDomain(user);
    }

    private static User mapToDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getLogin(),
                entity.getPasswordHash(),
                entity.getAge(),
                UserRole.valueOf(entity.getRole().toString())
        );
    }

}


