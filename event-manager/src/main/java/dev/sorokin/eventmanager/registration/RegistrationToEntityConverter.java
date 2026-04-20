package dev.sorokin.eventmanager.registration;

import org.springframework.stereotype.Component;

@Component
public class RegistrationToEntityConverter {

    public Registration toDomain(RegistrationEntity registrationEntity){
        return new Registration(
                registrationEntity.getId(),
                null,
                registrationEntity.getUserId(),
                registrationEntity.getCreated()
        );
    }

    public RegistrationEntity toEntity(Registration registration){
        return new RegistrationEntity(
                registration.id(),
                null,
                registration.userId(),
                registration.created()
        );
    }
}
