package dev.sorokin.eventmanager.event;

import dev.sorokin.eventmanager.security.jwt.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/events")
public class EventController {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    private final AuthenticationService authenticationService;

    private final EventService eventService;

    private final EventToDtoMapper eventToDtoMapper;

    private final RequestDtoToEventDtoMapper requestDtoToEventDtoMapper;

    private final UpdateDtoToEventDtoMapper updateDtoToEventDtoMapper;


    public EventController(
            AuthenticationService authenticationService,
            EventService eventService,
            EventToDtoMapper eventToDtoMapper,
            RequestDtoToEventDtoMapper requestDtoToEventDtoMapper,
            UpdateDtoToEventDtoMapper updateDtoToEventDtoMapper) {
        this.authenticationService = authenticationService;
        this.eventService = eventService;
        this.eventToDtoMapper = eventToDtoMapper;
        this.requestDtoToEventDtoMapper = requestDtoToEventDtoMapper;
        this.updateDtoToEventDtoMapper = updateDtoToEventDtoMapper;
    }


    @PostMapping()
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<EventCreateRequestDto> createEvent(
            @RequestBody @Valid EventCreateRequestDto eventCreateRequestDto
    ) {
        var authUser = authenticationService.getCurrentAuthenticatedUserOrThrow();

        EventDto eventDto = requestDtoToEventDtoMapper.toEventDto(eventCreateRequestDto);

        Event eventToSave = eventService.createEvent(eventToDtoMapper.toDomain(eventDto), authUser);

        LOGGER.info("Created event: {}", eventToSave);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(requestDtoToEventDtoMapper.toRequestDto(eventToDtoMapper.toDto(eventToSave)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority({'USER', 'ADMIN'})")
    public ResponseEntity<EventDto> getEvent(@PathVariable("id") Long id) {
        Event event = eventService.getEventById(id);
        LOGGER.info("Retrieved event: {}", id);
        return ResponseEntity.ok(eventToDtoMapper.toDto(event));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority({'USER', 'ADMIN'})")
    public ResponseEntity<EventUpdateRequestDto> updateEvent(
            @PathVariable("id") Long id,
            @RequestBody @Valid EventUpdateRequestDto eventUpdateRequestDto) {
        var authUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        LOGGER.info("Updating event: {}", id);
        Event event = eventService.updateEvent(updateDtoToEventDtoMapper.toEventDto(eventUpdateRequestDto), authUser, id);
        return ResponseEntity.ok(updateDtoToEventDtoMapper.toUpdateDto(eventToDtoMapper.toDto(event)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority({'USER', 'ADMIN'})")
    public ResponseEntity<Void> deleteEvent(@PathVariable("id") Long id) {
        var authUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        LOGGER.info("Deleting event: {}", id);
        eventService.deleteEvent(id, authUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('USER')")
    public List<EventDto> getMyEvent() {
        var authUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        return eventService.getAllMyEvents(authUser)
                .stream()
                .map(eventToDtoMapper::toDto)
                .toList();
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority({'USER', 'ADMIN'})")
    public List<EventDto> searchFilter(@RequestBody EventSearchRequestDto eventSearchRequestDto){
        return eventService.searchFilter(eventSearchRequestDto).stream().map(eventToDtoMapper::toDto).toList();
    }

}
