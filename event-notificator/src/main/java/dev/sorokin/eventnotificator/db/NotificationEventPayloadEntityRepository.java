package dev.sorokin.eventnotificator.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationEventPayloadEntityRepository extends JpaRepository<NotificationEventPayloadEntity, Long> {


    NotificationEventPayloadEntity findByMessageId(UUID messageId);

    boolean existsByMessageId(UUID messageId);
}
