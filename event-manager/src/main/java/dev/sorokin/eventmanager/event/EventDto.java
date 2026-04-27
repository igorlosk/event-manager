package dev.sorokin.eventmanager.event;

import dev.sorokin.eventmanager.registration.RegistrationDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

public record EventDto(
        Integer id,
        @NotBlank
        String name,
        Integer ownerId,
        @Min(1)
        Integer maxPlaces,
        Integer occupiedPlaces,
        @NotBlank
        String date,
        @Min(1)
        Integer cost,
        @Min(30)
        Integer duration,
        Integer locationId,
        EventStatus status
) {
}
