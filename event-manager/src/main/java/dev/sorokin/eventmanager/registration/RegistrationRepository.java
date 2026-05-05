package dev.sorokin.eventmanager.registration;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {


    boolean existsByUserIdAndEventId(Long userId, Integer eventId);

    @Query("SELECT r FROM RegistrationEntity r JOIN FETCH r.event WHERE r.userId = :userId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<RegistrationEntity> findAllByUserId(@Param("userId") Long userId);


    RegistrationEntity findRegistrationEntitiesByUserIdAndEventId(Long userId, Integer event_id);

}