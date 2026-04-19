package dev.sorokin.eventmanager.events;

import dev.sorokin.eventmanager.registration.RegistrationDto;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;
import java.util.List;

public record EventDto(
        Long id,
        String name,
        Integer ownerId,
        Integer maxPlaces,
        Integer occupiedPlaces,
        LocalDateTime date,
        @Min(1)
        Integer cost,
        @Min(30)
        Integer duration,
        Integer locationId,
        EventStatus status,
        List<RegistrationDto> registrations
) {
}
