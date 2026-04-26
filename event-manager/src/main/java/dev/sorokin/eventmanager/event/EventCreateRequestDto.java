package dev.sorokin.eventmanager.event;

import jakarta.validation.constraints.Min;

public record EventCreateRequestDto(
        String name,
        Integer maxPlaces,
        String date,
        @Min(1)
        Integer cost,
        @Min(30)
        Integer duration,
        Integer locationId
) {
}
