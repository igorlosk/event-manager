package dev.sorokin.eventmanager.registration;

import dev.sorokin.eventmanager.security.jwt.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;

    public RegistrationController(RegistrationService registrationService, AuthenticationService authenticationService) {
        this.registrationService = registrationService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> registrationToEvent(@PathVariable("id") Long eventId){
        var authorisedUser = authenticationService.getCurrentAuthenticatedUserOrThrow();

        registrationService.registerToEvent(authorisedUser, eventId);

        return ResponseEntity.status(HttpStatus.CREATED).build();


    }


}
