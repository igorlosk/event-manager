package dev.sorokin.eventnotificator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import org.springframework.stereotype.Service;

@Service
public class NotificationEventPayloadService {

    private final NotificationEventPayloadEntityRepository notificationEventPayloadEntityRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public NotificationEventPayloadService(NotificationEventPayloadEntityRepository notificationEventPayloadEntityRepository) {
        this.notificationEventPayloadEntityRepository = notificationEventPayloadEntityRepository;
    }

    public void saveNotifications(EventChangeKafkaMessage value) {

        if(!notificationEventPayloadEntityRepository.existsByMessageId(value.messageId())){
            EventPayload eventPayload = new EventPayload(
                    value.eventName(),
                    value.changedById(),
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
                    toJson(eventPayload)
            );
            notificationEventPayloadEntityRepository.save(eventPayloadEntity);
        }
    }

    public String toJson(EventPayload eventPayload) {
        try {
            return objectMapper.writeValueAsString(eventPayload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка преобразования объекта в JSON", e);
        }
    }
}


