package dev.sorokin.eventmanager.events;

import dev.sorokin.eventmanager.registration.RegistrationToEntityConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventToEntityConverter {

    private final RegistrationToEntityConverter registrationToEntityConverter;

    public EventToEntityConverter(RegistrationToEntityConverter registrationToEntityConverter) {
        this.registrationToEntityConverter = registrationToEntityConverter;
    }

    public Event toDomain(EventEntity entity) {
        return new Event(
                entity.getId(),
                entity.getName(),
                entity.getMaxPlaces(),
                entity.getDate(),
                entity.getCost(),
                entity.getDuration(),
                entity.getLocationId(),
                entity.getStatus(),
                entity.getRegistrations() == null ? List.of() :
                        entity.getRegistrations()
                                .stream()
                                .map(registrationToEntityConverter::toDomain)
                                .toList()
        );
    }

    public EventEntity toEntity(Event event) {
        return new EventEntity(
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
                                .map(registrationToEntityConverter::toEntity)
                                .toList()
        );
    }
}
