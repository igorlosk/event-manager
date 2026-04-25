package dev.sorokin.eventmanager.event;

import dev.sorokin.eventmanager.registration.RegistrationToEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class EventToEntityMapper {

    private final RegistrationToEntityMapper registrationToEntityMapper;

    public EventToEntityMapper(RegistrationToEntityMapper registrationToEntityMapper) {
        this.registrationToEntityMapper = registrationToEntityMapper;
    }

    public EventEntity toEntity(Event event) {
        return new EventEntity(
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
                event.registrations()
                        .stream()
                        .map(registrationToEntityMapper::toEntity)
                        .toList()
        );
    }

    public Event toDomain(EventEntity eventEntity) {
        return new Event(
                eventEntity.getId(),
                eventEntity.getName(),
                eventEntity.getOwnerId(),
                eventEntity.getMaxPlaces(),
                eventEntity.getOccupiedPlaces(),
                eventEntity.getDate(),
                eventEntity.getCost(),
                eventEntity.getDuration(),
                eventEntity.getLocationId(),
                eventEntity.getStatus(),
                eventEntity.getRegistrations()
                        .stream()
                        .map(registrationToEntityMapper::toDomain)
                        .toList()
        );
    }
}
