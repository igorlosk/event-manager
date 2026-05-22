package dev.sorokin.eventnotificator;

import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

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

        value.subscribers().forEach(subscriberId ->
                notificationEntityRepository.save(new NotificationEntity(
                        null,
                        subscriberId,
                        false,
                        value.occurredAt(),
                        null,
                        eventPayloadEntity
                ))
        );

    }
}
