package dev.sorokin.eventmanager.registration.domain;

import dev.sorokin.eventmanager.event.db.EventEntity;

import java.time.LocalDateTime;

public record Registration(
        Long id,
        Long userId,
        LocalDateTime createdAt,
        EventEntity event
) {

}
