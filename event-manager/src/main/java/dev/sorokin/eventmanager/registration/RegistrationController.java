package dev.sorokin.eventmanager.registration;

import dev.sorokin.eventmanager.events.EventDto;
import dev.sorokin.eventmanager.events.EventToDtoConverter;
import dev.sorokin.eventmanager.security.jwt.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;
    private final EventToDtoConverter eventToDtoConverter;

    public RegistrationController(RegistrationService registrationService, AuthenticationService authenticationService, EventToDtoConverter eventToDtoConverter) {
        this.registrationService = registrationService;
        this.authenticationService = authenticationService;
        this.eventToDtoConverter = eventToDtoConverter;
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> registrationToEvent(@PathVariable("id") Long eventId) {
        var authorisedUser = authenticationService.getCurrentAuthenticatedUserOrThrow();

        registrationService.registerToEvent(authorisedUser, eventId);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('USER')")
    public List<EventDto> getAllEvents() {
        var authorisedUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        return registrationService.getAllEvents(authorisedUser.id())
                .stream()
                .map(eventToDtoConverter::toDto)
                .toList();

    }

}
