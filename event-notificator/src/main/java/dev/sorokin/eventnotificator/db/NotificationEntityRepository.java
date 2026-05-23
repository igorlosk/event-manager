package dev.sorokin.eventnotificator.db;

import dev.sorokin.eventnotificator.api.NotificationResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, Long> {

//    @Query("SELECT r FROM RegistrationEntity r JOIN FETCH r.event WHERE r.userId = :userId")

    @Query("SELECT n from  NotificationEntity n JOIN FETCH n.payload WHERE n.userId =:userId")
    List<NotificationEntity> findAllByUserId(Long userId);
}
