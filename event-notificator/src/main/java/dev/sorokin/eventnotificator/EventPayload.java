package dev.sorokin.eventnotificator;

import dev.sorokin.eventcommon.kafka.ChangeItem;

import java.util.List;

public record EventPayload(
        String eventName,
        Long changedById,
        List<ChangeItem> changeItem
) {
}
