package dev.sorokin.eventnotificator.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sorokin.eventnotificator.db.NotificationEventPayloadEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationPayloadMapper {

    private final ObjectMapper objectMapper;

    public NotificationPayloadMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public NotificationPayload toNotificationPayload(NotificationEventPayloadEntity entity) {
        try {
            Map<String, Object> payloadMap = objectMapper.readValue(
                    entity.getPayloadJson(),
                    new TypeReference<Map<String, Object>>() {}
            );

            payloadMap.put("messageId", entity.getMessageId().toString());
            payloadMap.put("eventType", entity.getEventType());
            payloadMap.put("occurredAt", entity.getOccurredAt().toString());
            payloadMap.put("changedById",
                    entity.getChangedById() != null ? entity.getChangedById().intValue() : null);
            payloadMap.put("ownerId",
                    entity.getOwnerId() != null ? entity.getOwnerId().intValue() : null);

            return objectMapper.convertValue(payloadMap, NotificationPayload.class);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка маппинга entity в NotificationPayload", e);
        }
    }
}
