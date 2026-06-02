package dev.sorokin.eventnotificator.user;

public record AuthUser(
        Long id,
        String login,
        String role
) {
}
