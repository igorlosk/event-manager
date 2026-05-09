package dev.sorokin.eventmanager.event.domain;

import dev.sorokin.eventmanager.registration.domain.Registration;

import java.time.LocalDateTime;
import java.util.List;

public record Event(
        Integer id,
        String name,
        Integer ownerId,
        Integer maxPlaces,
        Integer occupiedPlaces,
        LocalDateTime date,
        Integer cost,
        Integer duration,
        Integer locationId,
        EventStatus status,
        List<Registration> registrations
) {

}
