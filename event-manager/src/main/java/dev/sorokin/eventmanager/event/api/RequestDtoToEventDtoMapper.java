package dev.sorokin.eventmanager.event.api;

import dev.sorokin.eventmanager.event.DateTimeConverter;
import org.springframework.stereotype.Component;

@Component
public class RequestDtoToEventDtoMapper {

    private final DateTimeConverter dateTimeConverter;

    public RequestDtoToEventDtoMapper(DateTimeConverter dateTimeConverter) {
        this.dateTimeConverter = dateTimeConverter;
    }

    public EventDto toEventDto(EventCreateRequestDto eventCreateRequestDto) {
        return new EventDto(
                null,
                eventCreateRequestDto.name(),
                null,
                eventCreateRequestDto.maxPlaces(),
                null,
                dateTimeConverter.formatToStringFromOffsetDateTime(eventCreateRequestDto.date()),
                eventCreateRequestDto.cost(),
                eventCreateRequestDto.duration(),
                eventCreateRequestDto.locationId(),
                null
        );
    }
}
