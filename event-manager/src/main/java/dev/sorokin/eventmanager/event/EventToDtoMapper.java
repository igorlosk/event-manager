package dev.sorokin.eventmanager.event;

import dev.sorokin.eventmanager.registration.RegistrationToDtoMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class EventToDtoMapper {

    private final RegistrationToDtoMapper registrationToDtoMapper;

    public EventToDtoMapper(RegistrationToDtoMapper registrationToDtoMapper) {
        this.registrationToDtoMapper = registrationToDtoMapper;
    }

    public EventDto toDto(Event event) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String dateTime = event.date().format(formatter);

        return new EventDto(
                event.id(),
                event.name(),
                event.ownerId(),
                event.maxPlaces(),
                event.occupiedPlaces(),
                dateTime,
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status(),
                event.registrations() == null ? List.of() :
                        event.registrations()
                                .stream()
                                .map(registrationToDtoMapper::toDto)
                                .toList()
        );
    }

    public Event toDomain(EventDto eventDto) {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        OffsetDateTime odt = OffsetDateTime.parse(eventDto.date(), formatter);

        LocalDateTime dateTime = odt.toLocalDateTime();

        return new Event(
                eventDto.id(),
                eventDto.name(),
                eventDto.ownerId(),
                eventDto.maxPlaces(),
                eventDto.occupiedPlaces(),
                dateTime,
                eventDto.cost(),
                eventDto.duration(),
                eventDto.locationId(),
                eventDto.status(),
                eventDto.registrations() == null ? List.of() :
                        eventDto.registrations()
                                .stream()
                                .map(registrationToDtoMapper::toDomain)
                                .toList()
        );
    }
}
