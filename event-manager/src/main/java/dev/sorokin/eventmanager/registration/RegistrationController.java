package dev.sorokin.eventmanager.registration;

import dev.sorokin.eventmanager.security.jwt.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events/registration")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;

    public RegistrationController(RegistrationService registrationService, AuthenticationService authenticationService) {
        this.registrationService = registrationService;
        this.authenticationService = authenticationService;
    }

    public ResponseEntity<RegistrationDto> registrationToEvent(@PathVariable Long eventId){

        var authUser = authenticationService.getCurrentAuthenticatedUserOrThrow();

        RegistrationDto registrationDto = registrationService.register(authUser.id(), eventId);

    }


}
