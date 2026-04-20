package dev.sorokin.eventmanager.registration;

import dev.sorokin.eventmanager.events.Event;
import dev.sorokin.eventmanager.events.EventService;
import dev.sorokin.eventmanager.events.EventStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class RegistrationService {

    private final EventService eventService;

    public RegistrationService(EventService eventService) {
        this.eventService = eventService;
    }

    @Transactional
    public void register(Long userId, Long eventId) {

        Event event = eventService.findEventById(eventId);

        if (!(event.status() == EventStatus.WAIT_START)) {
            throw new IllegalArgumentException("Registration is prohibited");
        }

        if (event.occupiedPlaces() > event.registrations().size()) {
            throw new IllegalArgumentException("Registration completed");
        }

        Registration registration = new Registration(
                null,
                event.id(),
                userId,
                LocalDateTime.now()
        );

        eventService.occupiedPlacesRefresh(eventId);


        // найти событие
// проверить статус
// проверить лимиты
// создать регистрацию
// обновить occupiedPlaces
    }
}
