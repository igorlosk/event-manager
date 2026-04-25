package dev.sorokin.eventmanager.event;

import dev.sorokin.eventmanager.registration.RegistrationDto;

import java.time.LocalDateTime;
import java.util.List;

public record EventDto(
        Integer id,
        String name,
        Integer ownerId,
        Integer maxPlaces,
        Integer occupiedPlaces,
        String date,
        Integer cost,
        Integer duration,
        Integer locationId,
        EventStatus status,
        List<RegistrationDto> registrations
) {
}
