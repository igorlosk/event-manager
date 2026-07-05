package dev.sorokin.eventnotificator.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("SELECT n from  NotificationEntity n JOIN FETCH n.payload WHERE n.userId =:userId")
    List<NotificationEntity> findAllByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE NotificationEntity n SET n.isRead = true, n.readAt = CURRENT_TIMESTAMP " +
            "WHERE n.id IN :ids AND n.userId = :userId")
    int markAsReadByIdsAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM NotificationEntity n WHERE n.payload.occurredAt <= :thresholdDate")
    int deleteNotificationsByPayloadDate(LocalDateTime thresholdDate);


    @Query("SELECT COUNT(n) FROM NotificationEntity n WHERE n.userId = :userId AND n.isRead = false")
    long countByUserIdAndReadFalse(@Param("userId") Long userId);
}
