package dev.sorokin.eventmanager.registration;

import org.springframework.stereotype.Component;

@Component
public class RegistrationToDtoMapper {

    public RegistrationDto toDto(Registration registration){
        return new RegistrationDto(
                registration.id(),
                registration.userId(),
                registration.createdAt(),
                registration.event()
        );
    }

    public Registration toDomain(RegistrationDto registrationDto){
        return new Registration(
                registrationDto.id(),
                registrationDto.userId(),
                registrationDto.createdAt(),
                registrationDto.event()
        );
    }
}
