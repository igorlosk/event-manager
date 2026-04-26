package dev.sorokin.eventmanager.event;

import dev.sorokin.eventmanager.location.Location;
import dev.sorokin.eventmanager.location.LocationService;
import dev.sorokin.eventmanager.users.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventToEntityMapper eventToEntityMapper;
    private final LocationService locationService;
    private final EventToDtoMapper eventToDtoMapper;

    public EventService(
            EventRepository eventRepository,
            EventToEntityMapper eventToEntityMapper,
            LocationService locationService, EventToDtoMapper eventToDtoMapper) {
        this.eventRepository = eventRepository;
        this.eventToEntityMapper = eventToEntityMapper;
        this.locationService = locationService;
        this.eventToDtoMapper = eventToDtoMapper;
    }


    @Transactional
    public Event createEvent(Event event, User user) {

        if (event.date().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Date cannot be before now");
        }

        Location location = locationService.getLocationById(event.locationId());

        if (location.capacity() < event.maxPlaces()) {
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

    public Event updateEvent(EventDto eventDto, User authUser, Long eventId) {

        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("No event with id " + eventId));

        Event event = eventToDtoMapper.toDomain(eventDto);

        Integer id = Math.toIntExact(eventId);

        EventEntity evenToUpdate = new EventEntity(
                id,
                event.name(),
                eventEntity.getOwnerId(),
                event.maxPlaces(),
                eventEntity.getOccupiedPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId(),
                eventEntity.getStatus(),
                eventEntity.getRegistrations()
        );

        eventRepository.save(evenToUpdate);

        return eventToEntityMapper.toDomain(evenToUpdate);

    }
}

