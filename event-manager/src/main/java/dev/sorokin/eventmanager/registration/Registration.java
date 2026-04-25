package dev.sorokin.eventmanager.registration;

import dev.sorokin.eventmanager.event.EventEntity;

import java.time.LocalDateTime;

public record Registration(
        Long id,
        Long userId,
        LocalDateTime createdAt,
        EventEntity event
) {

}
