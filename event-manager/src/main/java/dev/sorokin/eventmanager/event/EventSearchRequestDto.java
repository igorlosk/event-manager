package dev.sorokin.eventmanager.event;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record EventSearchRequestDto(
        @NotBlank
        String name,
        @Min(1)
        Integer placesMin,
        @Min(1)
        Integer placesMax,
        @NotBlank
        String dateStartAfter,
        @NotBlank
        String dateStartBefore,
        @Min(1)
        Integer costMin,
        @Min(1)
        Integer costMax,
        @Min(30)
        Integer durationMin,
        @Min(30)
        Integer durationMax,
        Integer locationId,
        EventStatus eventStatus
) {
}
