package dev.sorokin.eventmanager.registration;

import dev.sorokin.eventmanager.events.EventEntity;


import java.time.LocalDateTime;

public record RegistrationDto(
        Long id,
        EventEntity event,
        Long userId,
        LocalDateTime created
) {
}
