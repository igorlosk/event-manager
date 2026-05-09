package dev.sorokin.eventmanager.registration.api;

import dev.sorokin.eventmanager.event.db.EventEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RegistrationDto(
        @NotNull
        Long id,
        @NotNull
        Long userId,
        @NotBlank
        LocalDateTime createdAt,
        EventEntity event
) {
}
