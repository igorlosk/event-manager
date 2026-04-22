package dev.sorokin.eventmanager.registration;

import org.springframework.stereotype.Component;

@Component
public class RegistrationToDtoConverter {

    public Registration toDomain(RegistrationDto registrationDto){
        return new Registration(
                registrationDto.id(),
                registrationDto.event(),
                registrationDto.userId(),
                registrationDto.created()
        );
    }

    public RegistrationDto toDto(Registration registration){
        return new RegistrationDto(
                registration.id(),
                registration.event(),
                registration.userId(),
                registration.created()
        );
    }
}
