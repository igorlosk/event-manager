package dev.sorokin.eventmanager.security.jwt;

import dev.sorokin.eventmanager.users.SignInRequest;
import dev.sorokin.eventmanager.users.User;
import dev.sorokin.eventmanager.users.UserEntity;
import dev.sorokin.eventmanager.users.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenManager jwtTokenManager;

    private final UserRepository userRepository;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenManager jwtTokenManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
        this.userRepository = userRepository;
    }

    public String authenticateUser(SignInRequest signInRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.login(),
                        signInRequest.password()
                )
        );
        UserEntity user = userRepository.findByLogin(signInRequest.login())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));
        return jwtTokenManager.generateJwtToken(signInRequest.login(), user.getId());
    }

    public User getCurrentAuthenticatedUserOrThrow() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Authentication not present");
        }

        return (User) authentication.getPrincipal();
    }
}
