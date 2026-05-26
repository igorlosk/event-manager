package dev.sorokin.eventnotificator.api;

import dev.sorokin.eventnotificator.domain.NotificationPayload;

public record NotificationResponseDto(
        Long notificationId,
        String type,
        Long eventId,
        String createdAt,
        boolean isRead,
        String message,
        NotificationPayload payload
) {
}
