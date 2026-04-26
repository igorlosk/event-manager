package dev.sorokin.eventmanager.event;

import dev.sorokin.eventmanager.location.Location;
import dev.sorokin.eventmanager.location.LocationService;
import dev.sorokin.eventmanager.users.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventToEntityMapper eventToEntityMapper;
    private final LocationService locationService;

    public EventService(
            EventRepository eventRepository,
            EventToEntityMapper eventToEntityMapper,
            LocationService locationService) {
        this.eventRepository = eventRepository;
        this.eventToEntityMapper = eventToEntityMapper;
        this.locationService = locationService;
    }


    @Transactional
    public Event createEvent(Event event, User user) {

        if (event.date().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Date cannot be before now");
        }

        Location location = locationService.getLocationById(event.locationId());

        if (location.capacity() < event.maxPlaces()){
            throw new IllegalArgumentException("The capacity of the location should not be " +
                    "less than the number of event participants");
        }

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

    public Event getEvenById(long id) {
        EventEntity eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No event with id " + id));
        return eventToEntityMapper.toDomain(eventEntity);
    }
}
