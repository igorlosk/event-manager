package dev.sorokin.eventnotificator.domain;

public record NotificationResponse(
        Integer notificationId,
        String type,
        Integer eventId,
        String createdAt,
        boolean isRead,
        String message,
        NotificationPayload payload
) {
}
