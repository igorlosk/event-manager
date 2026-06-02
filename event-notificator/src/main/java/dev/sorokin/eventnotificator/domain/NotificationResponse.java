package dev.sorokin.eventnotificator.domain;

public record NotificationResponse(
        Long notificationId,
        String type,
        Long eventId,
        String createdAt,
        boolean isRead,
        String message,
        NotificationPayload payload
) {
}
