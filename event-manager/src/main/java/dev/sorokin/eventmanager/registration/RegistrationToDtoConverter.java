package dev.sorokin.eventmanager.registration;

import org.springframework.stereotype.Component;

@Component
public class RegistrationToDtoConverter {

    public Registration toDomain(RegistrationDto registrationDto){
        return new Registration(
                registrationDto.id(),
                registrationDto.event_id(),
                registrationDto.userId(),
                registrationDto.created()
        );
    }

    public RegistrationDto toDto(Registration registration){
        return new RegistrationDto(
                registration.id(),
                registration.event_id(),
                registration.userId(),
                registration.created()
        );
    }
}
