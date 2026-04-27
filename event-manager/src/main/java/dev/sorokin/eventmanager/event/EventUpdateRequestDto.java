package dev.sorokin.eventmanager.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record EventUpdateRequestDto(
        @NotBlank
        String name,
        @Min(1)
        Integer maxPlaces,
        @NotBlank
        String date,
        @Min(1)
        Integer cost,
        @Min(30)
        Integer duration,
        Integer locationId
) {
}
