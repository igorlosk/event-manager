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

    public Event toDomain(EventCreateRequestDto eventCreateRequestDto) {
        return new Event(
                eventCreateRequestDto.id(),
                eventCreateRequestDto.name(),
                eventCreateRequestDto.maxPlaces(),
                eventCreateRequestDto.date(),
                eventCreateRequestDto.cost(),
                eventCreateRequestDto.duration(),
                eventCreateRequestDto.locationId(),
                eventCreateRequestDto.status(),
                eventCreateRequestDto.registrations() == null ? List.of() :
                        eventCreateRequestDto.registrations()
                                .stream()
                                .map(registrationToDtoConverter::toDomain)
                                .toList()

        );
    }

    public EventCreateRequestDto toDto(Event event) {
        return new EventCreateRequestDto(
                event.id(),
                event.name(),
                event.maxPlaces(),
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
