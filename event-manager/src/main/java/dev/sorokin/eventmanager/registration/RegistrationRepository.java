package dev.sorokin.eventmanager.registration;

import dev.sorokin.eventmanager.users.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {


    @Query("SELECT COUNT(r) > 0 FROM RegistrationEntity r " +
            "WHERE r.userId = :userId AND r.id = :eventId")
    boolean existsByUserIdAndEventId(@Param("userId") Long userId,
                                     @Param("eventId") Long eventId);

    @Query("SELECT r FROM RegistrationEntity r JOIN FETCH r.event WHERE r.userId = :userId")
    List<RegistrationEntity> findAllByUserId(@Param("userId") Long userId);
}

