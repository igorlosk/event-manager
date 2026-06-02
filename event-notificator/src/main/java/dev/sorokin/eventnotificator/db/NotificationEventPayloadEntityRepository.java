package dev.sorokin.eventnotificator.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.UUID;

public interface NotificationEventPayloadEntityRepository extends JpaRepository<NotificationEventPayloadEntity, Long> {


    NotificationEventPayloadEntity findByMessageId(UUID messageId);

    boolean existsByMessageId(UUID messageId);

    @Modifying
    @Query("DELETE FROM NotificationEventPayloadEntity e WHERE e.occurredAt <= :thresholdDate")
    int deletePayload(LocalDateTime thresholdDate);
}
