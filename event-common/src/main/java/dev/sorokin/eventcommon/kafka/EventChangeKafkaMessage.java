package dev.sorokin.eventcommon.kafka;

import org.apache.kafka.common.protocol.types.Field;

import java.time.LocalDateTime;
import java.util.List;



public record EventChangeKafkaMessage(
        Field.UUID messageId,
        String eventType,
        Long eventId,
        LocalDateTime occurredAt,
        Long ownerId,
        Long changedById,
        List<Long> subscribers,
        List<ChangeItem> changes
) {

}
