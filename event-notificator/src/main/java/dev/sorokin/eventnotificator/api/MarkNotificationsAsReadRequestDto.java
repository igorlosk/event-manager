package dev.sorokin.eventnotificator.api;

import java.util.List;

public record MarkNotificationsAsReadRequestDto(
        List<Long> notificationIds
) {
}
