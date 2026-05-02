package dev.sorokin.eventmanager.registration;

import dev.sorokin.eventmanager.users.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {


    boolean existsByUserIdAndEventId(Long userId, Integer eventId);

    @Query("SELECT r FROM RegistrationEntity r JOIN FETCH r.event WHERE r.userId = :userId")
    List<RegistrationEntity> findAllByUserId(@Param("userId") Long userId);


    RegistrationEntity findRegistrationEntitiesByUserIdAndEventId(Long userId, Integer event_id);

}