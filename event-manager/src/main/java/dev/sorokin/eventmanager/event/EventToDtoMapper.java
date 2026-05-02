package dev.sorokin.eventmanager.event;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventToDtoMapper {

    private final DateTimeConverter dateTimeConverter;

    public EventToDtoMapper(DateTimeConverter dateTimeConverter) {
        this.dateTimeConverter = dateTimeConverter;
    }

    public EventDto toDto(Event event) {

        String localDateTimeString = dateTimeConverter.formatToString(event.date());

        return new EventDto(
                event.id(),
                event.name(),
                event.ownerId(),
                event.maxPlaces(),
                event.occupiedPlaces(),
                localDateTimeString,
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status()
        );
    }

    public Event toDomain(EventDto eventDto) {

        LocalDateTime localDateTime = dateTimeConverter.parseToLocalDateTime(eventDto.date());

        return new Event(
                eventDto.id(),
                eventDto.name(),
                eventDto.ownerId(),
                eventDto.maxPlaces(),
                eventDto.occupiedPlaces(),
                localDateTime,
                eventDto.cost(),
                eventDto.duration(),
                eventDto.locationId(),
                eventDto.status(),
                null
        );
    }
}
