package dev.sorokin.eventnotificator.api;

import dev.sorokin.eventnotificator.domain.NotificationResponse;
import org.springframework.stereotype.Component;

@Component
public class NotificationResponseToDtoMapper {

    public NotificationResponseDto toDto(NotificationResponse notificationResponse) {
        return new NotificationResponseDto(
                notificationResponse.notificationId(),
                notificationResponse.type(),
                notificationResponse.eventId(),
                notificationResponse.createdAt(),
                notificationResponse.isRead(),
                notificationResponse.message(),
                notificationResponse.payload()
        );
    }
}
