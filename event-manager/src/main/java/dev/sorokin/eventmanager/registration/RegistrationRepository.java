package dev.sorokin.eventmanager.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {


    @Query("SELECT COUNT(r) > 0 FROM RegistrationEntity r " +
            "WHERE r.userId = :userId AND r.eventId = :eventId")
    boolean existsByUserIdAndEventId(@Param("userId") Long userId,
                                     @Param("eventId") Long eventId);
}
