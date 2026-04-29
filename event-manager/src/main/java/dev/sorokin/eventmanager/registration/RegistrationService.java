package dev.sorokin.eventmanager.registration;

import dev.sorokin.eventmanager.event.*;
import dev.sorokin.eventmanager.users.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class RegistrationService {

    private final EventRepository eventRepository;

    private final RegistrationRepository registrationRepository;

    private final RegistrationToEntityMapper registrationToEntityMapper;

    public RegistrationService(
            EventRepository eventRepository,
            RegistrationRepository registrationRepository, RegistrationToEntityMapper registrationToEntityMapper
    ) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.registrationToEntityMapper = registrationToEntityMapper;
    }

    @Transactional
    public void registerToEvent(User authUser, Long eventId) {
        EventEntity eventToRegister = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Event with ID %d not found", eventId)));

        if (eventToRegister.getStatus() != EventStatus.WAIT_START) {
            throw new IllegalArgumentException(
                    String.format("Cannot register to event %d: status is %s",
                            eventId, eventToRegister.getStatus()));
        }

        boolean isAlreadyRegistered = registrationRepository
                .existsByUserIdAndEventId(authUser.id(), eventId);
        if (isAlreadyRegistered) {
            throw new IllegalArgumentException(
                    String.format("User %d is already registered to event %d",
                            authUser.id(), eventId));
        }

        eventToRegister.setOccupiedPlaces(eventToRegister.getOccupiedPlaces() + 1);
        eventRepository.save(eventToRegister);

        RegistrationEntity registrationEntity = new RegistrationEntity(
                null,
                authUser.id(),
                LocalDateTime.now(),
                eventToRegister
        );

        registrationRepository.save(registrationEntity);

    }

    public List<Registration> getAllMyEvents(User authUser) {
        return registrationRepository.findAllByUserId(authUser.id())
                .stream()
                .map(registrationToEntityMapper::toDomain)
                .toList();

    }

    public void deleteRegistration(Long id, User userAuth) {

        EventEntity eventToRegister = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Event with ID %d not found", id)));

        if (eventToRegister.getStatus() != EventStatus.WAIT_START) {
            throw new IllegalArgumentException(
                    String.format("Cannot delete event %d: status is %s",
                            id, eventToRegister.getStatus()));
        }

        RegistrationEntity registrationEntity = registrationRepository
                .findRegistrationEntitiesByUserIdAndEventId(userAuth.id(), eventToRegister.getId());

        if (!registrationEntity.getUserId().equals(userAuth.id())) {
            throw new SecurityException("Cannot delete another user's registration");
        }

        if (registrationEntity == null) {
            throw new EntityNotFoundException("Registration not found");
        }

        registrationRepository.delete(registrationEntity);

    }
}