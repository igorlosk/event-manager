package dev.sorokin.eventmanager.event.api;

import dev.sorokin.eventmanager.event.domain.EventStatus;

public record EventSearchRequestDto(
        String name,
        Integer placesMin,
        Integer placesMax,
        String dateStartAfter,
        String dateStartBefore,
        Integer costMin,
        Integer costMax,
        Integer durationMin,
        Integer durationMax,
        Integer locationId,
        EventStatus eventStatus
) {
}
