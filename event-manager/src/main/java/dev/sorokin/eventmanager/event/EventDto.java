package dev.sorokin.eventmanager.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record EventDto(
        @NotNull
        Integer id,
        @NotBlank
        String name,
        @NotNull
        Integer ownerId,
        @Min(1)
        Integer maxPlaces,
        @PositiveOrZero
        Integer occupiedPlaces,
        @NotBlank
        String date,
        @Min(1)
        Integer cost,
        @Min(30)
        Integer duration,
        @NotNull
        Integer locationId,
        EventStatus status
) {
}
