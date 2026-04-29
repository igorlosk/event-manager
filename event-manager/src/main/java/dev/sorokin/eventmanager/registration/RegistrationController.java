package dev.sorokin.eventmanager.registration;

import dev.sorokin.eventmanager.event.*;
import dev.sorokin.eventmanager.security.jwt.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/registrations")
public class RegistrationController {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    private final AuthenticationService authenticationService;

    private final RegistrationService registrationService;

    private final RegistrationToDtoMapper registrationToDtoMapper;


    public RegistrationController(
            AuthenticationService authenticationService,
            RegistrationService registrationService, RegistrationToDtoMapper registrationToDtoMapper
    ) {
        this.authenticationService = authenticationService;
        this.registrationService = registrationService;
        this.registrationToDtoMapper = registrationToDtoMapper;
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> createRegistration(
            @PathVariable("id") Long id
    ) {
        var authUser = authenticationService.getCurrentAuthenticatedUserOrThrow();

        registrationService.registerToEvent(authUser, id);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('USER')")
    List<RegistrationDto> getMyRegistEvent() {
        var authUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        return registrationService.getAllMyEvents(authUser)
                .stream()
                .map(registrationToDtoMapper::toDto)
                .toList();
    }

    @DeleteMapping("/cancel/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> deleteRegistration(
            @PathVariable("id") Long id
    ){
        var authUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        registrationService.deleteRegistration(id, authUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
