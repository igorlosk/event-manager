package dev.sorokin.eventnotificator.domain;

import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import dev.sorokin.eventnotificator.db.NotificationEntity;
import dev.sorokin.eventnotificator.db.NotificationEntityRepository;
import dev.sorokin.eventnotificator.db.NotificationEventPayloadEntity;
import dev.sorokin.eventnotificator.db.NotificationEventPayloadEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationEventPayloadEntityRepository notificationEventPayloadEntityRepository;

    private final NotificationEntityRepository notificationEntityRepository;

    public NotificationService(
            NotificationEventPayloadEntityRepository notificationEventPayloadEntityRepository,
            NotificationEntityRepository notificationEntityRepository) {
        this.notificationEventPayloadEntityRepository = notificationEventPayloadEntityRepository;
        this.notificationEntityRepository = notificationEntityRepository;
    }

    public void createNotificationForUsers(EventChangeKafkaMessage value) {
        NotificationEventPayloadEntity eventPayloadEntity =
                notificationEventPayloadEntityRepository.findByMessageId(value.messageId());

        value.subscribers().forEach(subscriberId -> {
                    notificationEntityRepository.save(new NotificationEntity(
                            null,
                            subscriberId,
                            false,
                            value.occurredAt(),
                            null,
                            eventPayloadEntity
                    ));
                    LOGGER.info("Created notification for user id={}", subscriberId);
                }
        );
    }

    public List<NotificationResponse> getAllNotifications() {

        return null;
    }
}
