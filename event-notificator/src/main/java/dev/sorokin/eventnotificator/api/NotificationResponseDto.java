package dev.sorokin.eventnotificator.api;

import dev.sorokin.eventnotificator.domain.NotificationPayload;

public record NotificationResponseDto(
        Integer notificationId,
        String type,
        Integer eventId,
        String createdAt,
        boolean isRead,
        String message,
        NotificationPayload payload
) {
}
