package dev.sorokin.eventmanager.events;

import dev.sorokin.eventmanager.location.LocationService;
import dev.sorokin.eventmanager.users.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final EventToEntityConverter toEntityConverter;

    public EventService(EventRepository eventRepository, LocationService locationService, EventToEntityConverter toEntityConverter) {
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.toEntityConverter = toEntityConverter;
    }

    public Event createEvent(Event event, User user) {

        var location = locationService.getLocationById(event.locationId());
        Integer userId = Math.toIntExact(user.id());

        var newEvent = new Event(
                null,
                event.name(),
                userId,
                event.maxPlaces(),
                0,
                event.date(),
                event.cost(),
                event.duration(),
                location.id(),
                EventStatus.WAIT_START,
                List.of()

        );

        EventEntity entityToSave = toEntityConverter.toEntity(newEvent);

        return toEntityConverter.toDomain(eventRepository.save(entityToSave));
    }
}
