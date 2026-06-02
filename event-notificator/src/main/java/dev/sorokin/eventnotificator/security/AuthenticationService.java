package dev.sorokin.eventnotificator.security;

import dev.sorokin.eventnotificator.user.AuthUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public AuthUser getAuthUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new IllegalStateException("Authentication not present in SecurityContextHolder");
        }
        return (AuthUser) auth.getPrincipal();
    }
}
