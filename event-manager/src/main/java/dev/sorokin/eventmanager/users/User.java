package dev.sorokin.eventmanager.users;

import jakarta.persistence.*;

public record User(
        Long id,
        String login,
        String passwordHash,
        Integer age,
        UserRole role
) {
}
