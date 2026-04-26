package dev.sorokin.eventmanager.event;

import dev.sorokin.eventmanager.location.LocationController;
import dev.sorokin.eventmanager.security.jwt.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/events")
public class EventController {

    private final static Logger LOGGER = LoggerFactory.getLogger(LocationController.class);

    private final AuthenticationService authenticationService;

    private final EventService eventService;

    private final EventToDtoMapper eventToDtoMapper;

    private final RequestDtoToEventDtoMapper requestDtoToEventDtoMapper;


    public EventController(
            AuthenticationService authenticationService,
            EventService eventService,
            EventToDtoMapper eventToDtoMapper,
            RequestDtoToEventDtoMapper requestDtoToEventDtoMapper) {
        this.authenticationService = authenticationService;
        this.eventService = eventService;
        this.eventToDtoMapper = eventToDtoMapper;
        this.requestDtoToEventDtoMapper = requestDtoToEventDtoMapper;
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

}
