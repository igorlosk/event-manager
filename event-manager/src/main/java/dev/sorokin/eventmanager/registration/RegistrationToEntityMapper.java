package dev.sorokin.eventmanager.registration;

import org.springframework.stereotype.Component;

@Component
public class RegistrationToEntityMapper {

    public RegistrationEntity toEntity(Registration registration) {
        return new RegistrationEntity(
                registration.id(),
                registration.userId(),
                registration.createdAt(),
                registration.event()
        );
    }

    public Registration toDomain(RegistrationEntity registrationEntity) {
        return new Registration(
                registrationEntity.getId(),
                registrationEntity.getUserId(),
                registrationEntity.getCreatedAt(),
                registrationEntity.getEvent()
        );
    }
}
