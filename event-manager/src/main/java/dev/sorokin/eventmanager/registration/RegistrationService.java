package dev.sorokin.eventmanager.registration;

import dev.sorokin.eventmanager.events.*;
import dev.sorokin.eventmanager.users.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Component
public class RegistrationService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public RegistrationService(EventRepository eventRepository,
                               RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void registerToEvent(User user, Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Event with ID %d not found", eventId)));

        // Проверка статуса события
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

        // Проверка свободных мест
        if (eventEntity.getOccupiedPlaces() >= eventEntity.getMaxPlaces()) {
            throw new IllegalArgumentException(
                    String.format("Event %d is full: %d/%d places occupied",
                            eventId,
                            eventEntity.getOccupiedPlaces(),
                            eventEntity.getMaxPlaces()));
        }



        // Увеличение счётчика занятых мест
        eventEntity.setOccupiedPlaces(eventEntity.getOccupiedPlaces() + 1);
        eventRepository.save(eventEntity);

        // Создание регистрации
        RegistrationEntity registration = new RegistrationEntity(
                null,
                eventId,
                user.id(),
                LocalDateTime.now()
        );
        registrationRepository.save(registration);
    }
}



//@Component
//public class RegistrationService {
//
//    private final EventRepository eventRepository;
//    private final RegistrationRepository registrationRepository;
//
//    public RegistrationService(EventRepository eventRepository, RegistrationRepository registrationRepository) {
//        this.eventRepository = eventRepository;
//        this.registrationRepository = registrationRepository;
//    }
//
//    @Transactional
//    public void registerToEvent(User user, Long eventId) {
//
//        EventEntity eventEntity = eventRepository.findById(eventId)
//                .orElseThrow(() -> new EntityNotFoundException("Not event found"));
//
//        if (!(eventEntity.getStatus() == EventStatus.WAIT_START)) {
//            throw new IllegalArgumentException("Registration is not in status to register");
//        }
//
//        RegistrationEntity registration = new RegistrationEntity(
//                null,
//                eventId,
//                user.id(),
//                LocalDateTime.now()
//        );
//        if (eventEntity.getOccupiedPlaces() >= eventEntity.getMaxPlaces()) {
//            throw new IllegalArgumentException("Registration is completed, no more free places");
//        }
//
//        eventEntity.setOccupiedPlaces(eventEntity.getOccupiedPlaces() + 1);
//
//        registrationRepository.save(registration);
//        eventRepository.save(eventEntity);
//    }
//}
// найти событие
// проверить статус
// проверить лимиты
// создать регистрацию
// обновить occupiedPlaces

