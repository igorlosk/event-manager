package dev.sorokin.eventmanager.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
        @NotNull
        Integer locationId
) {
}
