package dev.sorokin.eventmanager.registration;

import dev.sorokin.eventmanager.events.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class RegistrationService {

    private final EventService eventService;
    private final RegistrationToEntityConverter toEntityConverter;
    private final EventToEntityConverter eventToEntityConverter;
    private final EventRepository eventRepository;

    public RegistrationService(EventService eventService, RegistrationToEntityConverter toEntityConverter, EventToEntityConverter eventToEntityConverter, EventRepository eventRepository) {
        this.eventService = eventService;
        this.toEntityConverter = toEntityConverter;
        this.eventToEntityConverter = eventToEntityConverter;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public void register(Long userId, Long eventId) {

        Event eventById = eventService.findEventById(eventId);
        EventEntity eventEntity = eventToEntityConverter.toEntity(eventById);


        if (!(eventById.status() == EventStatus.WAIT_START)) {
            throw new IllegalArgumentException("Registration is prohibited");
        }

        if (eventById.occupiedPlaces() > eventById.registrations().size()) {
            throw new IllegalArgumentException("Registration completed");
        }

        Registration registration = new Registration(
                null,
                eventEntity,
                userId,
                LocalDateTime.now()
        );

        eventEntity.addRegistrationToEvent(toEntityConverter.toEntity(registration));

        eventRepository.save(eventEntity);



        // найти событие
// проверить статус
// проверить лимиты
// создать регистрацию
// обновить occupiedPlaces
    }
}
