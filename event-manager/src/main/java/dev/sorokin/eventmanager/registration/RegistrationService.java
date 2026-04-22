package dev.sorokin.eventmanager.registration;

import dev.sorokin.eventmanager.events.*;
import dev.sorokin.eventmanager.users.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RegistrationService {

    private final EventService eventService;
    private final RegistrationToEntityConverter toEntityConverter;
    private final EventToEntityConverter eventToEntityConverter;
    private final EventRepository eventRepository;
    private final RegistrationToDtoConverter registrationToDtoConverter;
    private final RegistrationRepository registrationRepository;

    public RegistrationService(EventService eventService, RegistrationToEntityConverter toEntityConverter, EventToEntityConverter eventToEntityConverter, EventRepository eventRepository, RegistrationToDtoConverter registrationToDtoConverter, RegistrationRepository registrationRepository) {
        this.eventService = eventService;
        this.toEntityConverter = toEntityConverter;
        this.eventToEntityConverter = eventToEntityConverter;
        this.eventRepository = eventRepository;
        this.registrationToDtoConverter = registrationToDtoConverter;
        this.registrationRepository = registrationRepository;
    }

    @Transactional
    public void registerToEvent(User user, Long eventId) {

        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Not event found"));

        RegistrationEntity registration = new RegistrationEntity(
                null,
                eventId,
                user.id(),
                LocalDateTime.now()
        );

        registrationRepository.save(registration);


        eventEntity.addRegistrationToEvent(registration);

        eventRepository.save(eventEntity);


        // найти событие
// проверить статус
// проверить лимиты
// создать регистрацию
// обновить occupiedPlaces
    }
}
