package dev.sorokin.eventmanager.events;

import dev.sorokin.eventmanager.security.jwt.AuthenticationService;
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

    private final EventToDtoConverter eventToDtoConverter;
    private final EventService eventService;
    private final AuthenticationService authenticationService;
    private final RequestDtoToDtoConverter requestDtoToDtoConverter;

    public EventController(EventToDtoConverter eventToDtoConverter, EventService eventService, AuthenticationService authenticationService, RequestDtoToDtoConverter requestDtoToDtoConverter) {
        this.eventToDtoConverter = eventToDtoConverter;
        this.eventService = eventService;
        this.authenticationService = authenticationService;
        this.requestDtoToDtoConverter = requestDtoToDtoConverter;
    }


    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<EventCreateRequestDto> eventCreate (
            @RequestBody EventCreateRequestDto eventCreateRequestDto
    ){
        var authUser = authenticationService.getCurrentAuthenticatedUserOrThrow();

        EventDto eventDto = requestDtoToDtoConverter.toEventDto(eventCreateRequestDto);

        Event createdEvent = eventService
                .createEvent(eventToDtoConverter.toDomain(eventDto), authUser);

        EventDto responseDto = eventToDtoConverter.toDto(createdEvent);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(requestDtoToDtoConverter.toEventCreateRequestDto(responseDto));
    }
}
