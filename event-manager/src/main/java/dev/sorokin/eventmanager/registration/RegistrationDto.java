package dev.sorokin.eventmanager.registration;

import dev.sorokin.eventmanager.events.Event;
import dev.sorokin.eventmanager.events.EventDto;
import dev.sorokin.eventmanager.events.EventEntity;


import java.time.LocalDateTime;

public record RegistrationDto(
        Long id,
        Long eventId,
        Long userId,
        LocalDateTime created
) {
}
