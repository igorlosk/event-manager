package dev.sorokin.eventnotificator.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("SELECT n from  NotificationEntity n JOIN FETCH n.payload WHERE n.userId =:userId")
    List<NotificationEntity> findAllByUserId(Long userId);
}
