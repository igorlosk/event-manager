package dev.sorokin.eventmanager.event;

import dev.sorokin.eventmanager.location.Location;
import dev.sorokin.eventmanager.location.LocationService;
import dev.sorokin.eventmanager.users.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class EventService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final EventToEntityMapper eventToEntityMapper;
    private final LocationService locationService;
    private final EventToDtoMapper eventToDtoMapper;
    private final EventPermissionService eventPermissionService;

    public EventService(
            EventRepository eventRepository,
            EventToEntityMapper eventToEntityMapper,
            LocationService locationService, EventToDtoMapper eventToDtoMapper,
            EventPermissionService eventPermissionService) {
        this.eventRepository = eventRepository;
        this.eventToEntityMapper = eventToEntityMapper;
        this.locationService = locationService;
        this.eventToDtoMapper = eventToDtoMapper;
        this.eventPermissionService = eventPermissionService;
    }

    @Transactional
    public Event createEvent(Event event, User user) {

        if (event.date().isBefore(LocalDateTime.now())) {
            LOGGER.warn("Attempt to create event with past date: {} for user: {}", event.date(), user.id());
            throw new IllegalArgumentException("Date cannot be before now. Provided date: " + event.date());
        }

        Location location = locationService.getLocationById(event.locationId());

        if (location.capacity() < event.maxPlaces()) {
            LOGGER.warn("Location {} capacity {} is less than maxPlaces {} for event by user {}",
                    location.id(), location.capacity(), event.maxPlaces(), user.id());
            throw new IllegalArgumentException(
                    "The capacity of the location (" + location.capacity() +
                            ") should not be less than the number of event participants (" +
                            event.maxPlaces() + ")"
            );
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
        LOGGER.info("Event with id {} created successfully by user {}", savedEntity.getId(), userId);
        return eventToEntityMapper.toDomain(savedEntity);
    }

    public Event getEvenById(Long id) {
        EventEntity eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No event with id " + id));
        return eventToEntityMapper.toDomain(eventEntity);
    }

    @Transactional
    public Event updateEvent(EventDto eventDto, User authUser, Long eventId) {

        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    LOGGER.warn("Attempt to update non-existent event with id: {}", eventId);
                    return new EntityNotFoundException("Event with id " + eventId + " not found");
                });

        if (!eventPermissionService.canModify(authUser, eventToEntityMapper.toDomain(eventEntity))) {
            LOGGER.warn("User {} attempted to update event {} without permission", authUser.id(), eventId);
            throw new AccessDeniedException("User does not have permission to update this event");
        }

        Event event = eventToDtoMapper.toDomain(eventDto);

        Integer id = Math.toIntExact(eventId);

        eventRepository.updateLocation(
                id,
                event.name(),
                event.maxPlaces(),
                event.date(),
                event.cost(),
                event.duration(),
                event.locationId()
        );

        LOGGER.info("Event with id {} successfully updated by user {}", eventId, authUser.id());

        return getEvenById(eventId);
    }

    @Transactional
    public void deleteEvent(Long id, User authUser) {

        EventEntity eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.warn("Attempt to delete non-existent event with id: {}", id);
                    return new EntityNotFoundException("Event with id " + id + " not found");
                });

        if (!eventPermissionService.canModify(authUser, eventToEntityMapper.toDomain(eventEntity))) {
            LOGGER.warn("User {} attempted to delete event {} without permission", authUser.id(), id);
            throw new AccessDeniedException("User does not have permission to delete this event");
        }

        eventRepository.delete(eventEntity);
        LOGGER.info("Event with id {} successfully deleted by user {}", id, authUser.id());
    }

    public List<Event> getAllMyEvents(User authUser) {

        Integer userId = Math.toIntExact(authUser.id());

        return eventRepository.findAllByOwnerId(userId)
                .stream()
                .map(eventToEntityMapper::toDomain)
                .toList();
    }

}

