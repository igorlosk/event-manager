package dev.sorokin.eventmanager.events;

import dev.sorokin.eventmanager.location.LocationService;
import dev.sorokin.eventmanager.users.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        if (event.date().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Date cannot be before now");
        }

        var location = locationService.getLocationById(event.locationId());

        if (location.capacity() < event.maxPlaces()){
            throw new IllegalArgumentException("The capacity of the location should not be less than the number of event participants");
        }
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


    public Event findEventById(Long eventId) {
        EventEntity eventEntity = eventRepository.findById(eventId).orElseThrow(() ->
                new EntityNotFoundException("Event does not exists by id=%s".formatted(eventId)));

        return toEntityConverter.toDomain(eventEntity);
    }

}
