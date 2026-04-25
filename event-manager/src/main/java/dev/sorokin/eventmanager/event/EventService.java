package dev.sorokin.eventmanager.event;

import dev.sorokin.eventmanager.location.Location;
import dev.sorokin.eventmanager.location.LocationService;
import dev.sorokin.eventmanager.users.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventToEntityMapper eventToEntityMapper;
    private final LocationService locationService;

    public EventService(EventRepository eventRepository, EventToEntityMapper eventToEntityMapper, LocationService locationService) {
        this.eventRepository = eventRepository;
        this.eventToEntityMapper = eventToEntityMapper;
        this.locationService = locationService;
    }


    @Transactional
    public Event createEvent(Event event, User user) {

        Location location = locationService.getLocationById(event.locationId());
        Integer userId = Math.toIntExact(user.id());

        Event newEvent = new Event(
                null,
                event.name(),
                userId,
                location.capacity(),
                0,
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                EventStatus.WAIT_START,
                List.of()

        );
        EventEntity savedEntity = eventRepository.save(eventToEntityMapper.toEntity(newEvent));
        return eventToEntityMapper.toDomain(savedEntity);
    }
}
