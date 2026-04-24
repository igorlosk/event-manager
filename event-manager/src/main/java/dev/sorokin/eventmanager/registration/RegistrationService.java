package dev.sorokin.eventmanager.registration;

import dev.sorokin.eventmanager.events.*;
import dev.sorokin.eventmanager.users.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Component
public class RegistrationService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final EventToEntityConverter eventToEntityConverter;

    public RegistrationService(EventRepository eventRepository,
                               RegistrationRepository registrationRepository, EventToEntityConverter eventToEntityConverter) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.eventToEntityConverter = eventToEntityConverter;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void registerToEvent(User user, Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Event with ID %d not found", eventId)));

        if (eventEntity.getStatus() != EventStatus.WAIT_START) {
            throw new IllegalArgumentException(
                    String.format("Cannot register to event %d: status is %s",
                            eventId, eventEntity.getStatus()));
        }

        boolean isAlreadyRegistered = registrationRepository
                .existsByUserIdAndEventId(user.id(), eventId);
        if (isAlreadyRegistered) {
            throw new IllegalArgumentException(
                    String.format("User %d is already registered to event %d",
                            user.id(), eventId));
        }

        if (eventEntity.getOccupiedPlaces() >= eventEntity.getMaxPlaces()) {
            throw new IllegalArgumentException(
                    String.format("Event %d is full: %d/%d places occupied",
                            eventId,
                            eventEntity.getOccupiedPlaces(),
                            eventEntity.getMaxPlaces()));
        }

        eventEntity.setOccupiedPlaces(eventEntity.getOccupiedPlaces() + 1);
        eventRepository.save(eventEntity);

        RegistrationEntity registration = new RegistrationEntity(
                null,
                eventId,
                user.id(),
                LocalDateTime.now()
        );
        registrationRepository.save(registration);
    }

    public List<Event> getAllEvents(Long userId) {
        return eventRepository.findAllEvents(userId)
                .stream()
                .map(eventToEntityConverter::toDomain)
                .toList();
    }

}