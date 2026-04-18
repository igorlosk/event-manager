package dev.sorokin.eventmanager.events;

import dev.sorokin.eventmanager.registration.Registration;

import java.time.LocalDateTime;
import java.util.List;

public record Event(
        Long id,
        String name,
        Integer maxPlaces,
        LocalDateTime date,
        Integer cost,
        Integer duration,
        Integer locationId,
        EventStatus status,
        List<Registration> registrations

) {
}
