package dev.sorokin.eventnotificator.domain;

import dev.sorokin.eventcommon.kafka.ChangeItem;

import java.util.List;

public record NotificationPayload(
        String messageId,
        String eventType,
        String occurredAt,
        Long changedById,
        Long ownerId,
        String eventName,
        List<ChangeItem> changeItem
) {
}
