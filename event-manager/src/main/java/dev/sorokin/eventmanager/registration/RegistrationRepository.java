package dev.sorokin.eventmanager.registration;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {
}
