package dev.sorokin.eventnotificator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface NotificationEventPayloadEntityRepository extends JpaRepository<NotificationEventPayloadEntity, Long> {

    @Modifying
    @Query(value = "INSERT INTO notification_event_payloads " +
            "(message_id, event_type, event_id, occurred_at, owner_id, changed_by_id, payload_json) " +
            "VALUES (:messageId, :eventType, :eventId, :occurredAt, :ownerId, :changedById, CAST(:payloadJson AS jsonb))",
            nativeQuery = true)
    void saveWithJsonb(@Param("messageId") UUID messageId,
                       @Param("eventType") String eventType,
                       @Param("eventId") Long eventId,
                       @Param("occurredAt") LocalDateTime occurredAt,
                       @Param("ownerId") Long ownerId,
                       @Param("changedById") Long changedById,
                       @Param("payloadJson") String payloadJson);

    boolean existsByMessageId(UUID messageId);
}
