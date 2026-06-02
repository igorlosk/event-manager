package dev.sorokin.eventnotificator.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import dev.sorokin.eventnotificator.db.NotificationEventPayloadEntity;
import dev.sorokin.eventnotificator.db.NotificationEventPayloadEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationEventPayloadService {

    private final static Logger LOGGER = LoggerFactory.getLogger(NotificationEventPayloadService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final NotificationEventPayloadEntityRepository notificationEventPayloadEntityRepository;
    private final DateTimeConverter dateTimeConverter;

    public NotificationEventPayloadService(NotificationEventPayloadEntityRepository notificationEventPayloadEntityRepository, DateTimeConverter dateTimeConverter) {
        this.notificationEventPayloadEntityRepository = notificationEventPayloadEntityRepository;
        this.dateTimeConverter = dateTimeConverter;
    }

    public void saveNotifications(EventChangeKafkaMessage value) {
        if(!notificationEventPayloadEntityRepository.existsByMessageId(value.messageId())){
            NotificationPayload notificationPayload = new NotificationPayload(
                    value.messageId().toString(),
                    value.eventType(),
                    dateTimeConverter.formatToString(value.occurredAt()),
                    value.changedById(),
                    value.ownerId(),
                    value.eventName(),
                    value.changes()
            );

            NotificationEventPayloadEntity eventPayloadEntity = new NotificationEventPayloadEntity(
                    null,
                    value.messageId(),
                    "EVENT_UPDATED",
                    value.eventId(),
                    value.occurredAt(),
                    value.ownerId(),
                    value.changedById(),
                    toString(notificationPayload)
            );
            notificationEventPayloadEntityRepository.save(eventPayloadEntity);
            LOGGER.info("Notification Event Payload Saved Successfully. MessageId={}", value.messageId());
        }
    }

    public String toString(NotificationPayload notificationPayload) {
        try {
            return objectMapper.writeValueAsString(notificationPayload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка преобразования объекта в JSON", e);
        }
    }
}


