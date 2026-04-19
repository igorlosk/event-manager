package dev.sorokin.eventmanager.events;

import dev.sorokin.eventmanager.registration.RegistrationToDtoConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventToDtoConverter {

    private final RegistrationToDtoConverter registrationToDtoConverter;

    public EventToDtoConverter(RegistrationToDtoConverter registrationToDtoConverter) {
        this.registrationToDtoConverter = registrationToDtoConverter;
    }

    public Event toDomain(EventDto eventDto) {
        return new Event(
                eventDto.id(),
                eventDto.name(),
                eventDto.ownerId(),
                eventDto.maxPlaces(),
                eventDto.occupiedPlaces(),
                eventDto.date(),
                eventDto.cost(),
                eventDto.duration(),
                eventDto.locationId(),
                eventDto.status(),
                eventDto.registrations() == null ? List.of() :
                        eventDto.registrations()
                                .stream()
                                .map(registrationToDtoConverter::toDomain)
                                .toList()
        );
    }

    public EventDto toDto(Event event) {
        return new EventDto(
                event.id(),
                event.name(),
                event.ownerId(),
                event.maxPlaces(),
                event.occupiedPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                event.status(),
                event.registrations() == null ? List.of() :
                        event.registrations()
                                .stream()
                                .map(registrationToDtoConverter::toDto)
                                .toList()
        );
    }
}
