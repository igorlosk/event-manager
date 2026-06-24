package dev.sorokin.eventmanager.event.domain;

import dev.sorokin.eventcommon.kafka.ChangeItem;
import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import dev.sorokin.eventmanager.config.CacheConfiguration;
import dev.sorokin.eventmanager.event.DateTimeConverter;
import dev.sorokin.eventmanager.event.EventChangeSender;
import dev.sorokin.eventmanager.event.api.EventDto;
import dev.sorokin.eventmanager.event.api.EventSearchRequestDto;
import dev.sorokin.eventmanager.event.db.EventEntity;
import dev.sorokin.eventmanager.event.db.EventRepository;
import dev.sorokin.eventmanager.event.db.EventToEntityMapper;
import dev.sorokin.eventmanager.location.Location;
import dev.sorokin.eventmanager.location.LocationService;
import dev.sorokin.eventmanager.registration.db.RegistrationEntity;
import dev.sorokin.eventmanager.users.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Service
public class EventService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final EventToEntityMapper eventToEntityMapper;
    private final LocationService locationService;
    private final EventPermissionService eventPermissionService;
    private final DateTimeConverter dateTimeConverter;
    private final EventChangeSender eventChangeSender;
    private final CacheManager cacheManager;

    public EventService(
            EventRepository eventRepository,
            EventToEntityMapper eventToEntityMapper,
            LocationService locationService,
            EventPermissionService eventPermissionService,
            DateTimeConverter dateTimeConverter,
            EventChangeSender eventChangeSender,
            CacheManager cacheManager) {
        this.eventRepository = eventRepository;
        this.eventToEntityMapper = eventToEntityMapper;
        this.locationService = locationService;
        this.eventPermissionService = eventPermissionService;
        this.dateTimeConverter = dateTimeConverter;
        this.eventChangeSender = eventChangeSender;
        this.cacheManager = cacheManager;
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
                event.maxPlaces(),
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

    @Cacheable(
            value = "events",
            key = "'id:' + #id"
    )
    public Event getEventById(Long id) {
        EventEntity eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No event with id " + id));
        LOGGER.info("Get event by id={}", id);
        return eventToEntityMapper.toDomain(eventEntity);
    }

    @CacheEvict(
            value = "events",
            key = "'id:' + #eventId"
    )
    public Event updateEvent(EventDto eventDto, User authUser, Long eventId) {

        EventEntity eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    LOGGER.warn("Attempt to update non-existent event with id: {}", eventId);
                    return new EntityNotFoundException("Event with id " + eventId + " not found");
                });

        if (!eventEntity.getStatus().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException(
                    String.format("Cannot update event %d: status is %s",
                            eventId, eventEntity.getStatus()));
        }

        if (!eventPermissionService.canModify(authUser, eventToEntityMapper.toDomain(eventEntity))) {
            LOGGER.warn("User {} attempted to update event {} without permission", authUser.id(), eventId);
            throw new AccessDeniedException("User does not have permission to update this event");
        }

        LocalDateTime eventDtoDate = dateTimeConverter.parseToLocalDateTime(eventDto.date());
        if (eventDtoDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    String.format("The date can't be earlier than it is now %s", LocalDateTime.now()));
        }

        if (eventDto.maxPlaces() < eventEntity.getOccupiedPlaces()) {
            throw new IllegalArgumentException(
                    String.format("MaxPlaces must be bigger or equals occupiedPlaces %d", eventEntity.getOccupiedPlaces()));
        }

        locationService.getLocationById(eventDto.locationId());

        eventRepository.updateEvent(
                Math.toIntExact(eventId),
                eventDto.name(),
                eventDto.maxPlaces(),
                dateTimeConverter.parseToLocalDateTime(eventDto.date()),
                eventDto.cost(),
                eventDto.duration(),
                eventDto.locationId()

        );

        EventEntity updatedEvent = eventRepository.findById(eventId).orElseThrow();
        LOGGER.info("Event with id {} successfully updated by user {}", eventId, authUser.id());

        List<Long> subscribers = eventEntity.getRegistrations().stream().map((RegistrationEntity::getUserId)).toList();
        Long eventOwnerId = Long.valueOf(eventEntity.getOwnerId());
        List<ChangeItem> changeItemList = compareEvents(eventDto, eventEntity);

        if (!changeItemList.isEmpty()) {
            eventChangeSender.sendChanges(new EventChangeKafkaMessage(
                    UUID.randomUUID(),
                    "EVENT_UPDATED",
                    eventEntity.getName(),
                    eventId,
                    LocalDateTime.now(),
                    eventOwnerId,
                    authUser.id(),
                    subscribers,
                    changeItemList
            ));
        }


        return eventToEntityMapper.toDomain(updatedEvent);
    }

    @CacheEvict(
            value = "events",
            key = "'id:' + #id"
    )
    @Transactional
    public void deleteEvent(Long id, User authUser) {

        EventEntity eventEntity = eventRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.warn("Attempt to delete non-existent event with id: {}", id);
                    return new EntityNotFoundException("Event with id " + id + " not found");
                });

        if (!eventEntity.getStatus().equals(EventStatus.WAIT_START)) {
            throw new IllegalArgumentException(
                    String.format("Cannot delete event %d: status is %s",
                            id, eventEntity.getStatus()));
        }

        if (!eventPermissionService.canModify(authUser, eventToEntityMapper.toDomain(eventEntity))) {
            LOGGER.warn("User {} attempted to delete event {} without permission", authUser.id(), id);
            throw new AccessDeniedException("User does not have permission to delete this event");
        }

        eventRepository.delete(eventEntity);
        LOGGER.info("Event with id {} successfully deleted by user {}", id, authUser.id());
    }


    @Cacheable(
            value = "events",
            key = "'all'"
    )
    @Transactional()
    public List<Event> getAllMyEvents(User authUser) {

        Integer userId = Math.toIntExact(authUser.id());

        LOGGER.info("Get owner events by userId={}", userId);
        return eventRepository.findAllByOwnerId(userId)
                .stream()
                .map(eventToEntityMapper::toDomain)
                .toList();
    }

    @Transactional()
    public List<Event> searchFilter(EventSearchRequestDto eventSearchRequestDto) {

        LocalDateTime dateTimeAfter = dateTimeConverter.parseToLocalDateTime(eventSearchRequestDto.dateStartAfter());
        LocalDateTime dateTimeBefore = dateTimeConverter.parseToLocalDateTime(eventSearchRequestDto.dateStartBefore());

        List<Event> list = eventRepository.search(
                eventSearchRequestDto.name(),
                eventSearchRequestDto.placesMin(),
                eventSearchRequestDto.placesMax(),
                dateTimeAfter,
                dateTimeBefore,
                eventSearchRequestDto.costMin(),
                eventSearchRequestDto.costMax(),
                eventSearchRequestDto.durationMin(),
                eventSearchRequestDto.durationMax(),
                Long.valueOf(eventSearchRequestDto.locationId()),
                eventSearchRequestDto.eventStatus()
        ).stream().map(eventToEntityMapper::toDomain).toList();

        LOGGER.info("Search events by filters");
        return list != null ? list : Collections.emptyList();
    }

    @Scheduled(cron = "${event.status.cron}")
    @Transactional
    public void updateStatuses() {
        LOGGER.info("Starting status update job");
        List<Long> started = eventRepository.findStartedEventsWithStatus(EventStatus.WAIT_START);
        if (!started.isEmpty()) {
            started.forEach(id -> {
                eventRepository.changeStatus(started, EventStatus.STARTED);
                sendMessage(started, EventStatus.WAIT_START, EventStatus.STARTED);
                cacheManager.getCache("events").evict("id:" + id);
                LOGGER.info("Updated {} events to STARTED", started.size());
            });

        }

        List<Long> finished = eventRepository.findFinishedEventsWithStatus(EventStatus.STARTED);
        if (!finished.isEmpty()) {
            finished.forEach(id -> {
                eventRepository.changeStatus(finished, EventStatus.FINISHED);
                sendMessage(started, EventStatus.STARTED, EventStatus.FINISHED);
                cacheManager.getCache("events").evict("id:" + id);
                LOGGER.info("Updated {} events to FINISHED", started.size());
            });

        }
    }

    private void sendMessage(List<Long> eventsToChangeId, EventStatus eventStatusOld, EventStatus eventStatusNew) {
        List<ChangeItem> changeItem = List.of(new ChangeItem("Status", eventStatusOld, eventStatusNew));
        List<EventEntity> entityList = eventRepository.findAllById(eventsToChangeId);
        entityList.forEach(eventEntity -> {
            eventChangeSender.sendChanges(new EventChangeKafkaMessage(
                    UUID.randomUUID(),
                    "EVENT_UPDATED",
                    eventEntity.getName(),
                    Long.valueOf(eventEntity.getId()),
                    LocalDateTime.now(),
                    Long.valueOf(eventEntity.getOwnerId()),
                    null,
                    eventEntity.getRegistrations().stream().map((RegistrationEntity::getUserId)).toList(),
                    changeItem
            ));
        });
    }

    public List<ChangeItem> compareEvents(EventDto eventDto, EventEntity eventEntity) {
        List<ChangeItem> list = new ArrayList<>();

        if (!eventDto.name().equals(eventEntity.getName())) {
            list.add(new ChangeItem(
                    "name",
                    eventEntity.getName(),
                    eventDto.name()
            ));

        }
        if (!eventDto.locationId().equals(eventEntity.getLocationId())) {
            list.add(new ChangeItem(
                    "locationId",
                    eventEntity.getLocationId(),
                    eventDto.locationId()
            ));
        }

        if (!eventDto.duration().equals(eventEntity.getDuration())) {
            list.add(new ChangeItem(
                    "duration",
                    eventEntity.getDuration(),
                    eventDto.duration()
            ));
        }

        if (!eventDto.cost().equals(eventEntity.getCost())) {
            list.add(new ChangeItem(
                    "cost",
                    eventEntity.getCost(),
                    eventDto.cost()
            ));
        }

        if (!eventDto.maxPlaces().equals(eventEntity.getMaxPlaces())) {
            list.add(new ChangeItem(
                    "maxPlace",
                    eventEntity.getMaxPlaces(),
                    eventDto.maxPlaces()
            ));
        }

        LocalDateTime eventDtoTime = dateTimeConverter.parseToLocalDateTime(eventDto.date());
        LocalDateTime eventEntityTime = eventEntity.getDate();

        if (!eventDtoTime.isEqual(eventEntityTime)) {
            list.add(new ChangeItem(
                    "date",
                    eventDtoTime,
                    eventEntityTime
            ));
        }

        return list;
    }
}

