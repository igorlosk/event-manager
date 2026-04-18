package dev.sorokin.eventmanager.events;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventCreateRequestDto(
        @NotBlank
        String name,
        @NotNull
        @Min(1)
        Integer maxPlaces,
        LocalDateTime date,
        @Min(1)
        Integer cost,
        @Min(1)
        Integer duration,
        @NotNull
        Integer locationId
) {
}
