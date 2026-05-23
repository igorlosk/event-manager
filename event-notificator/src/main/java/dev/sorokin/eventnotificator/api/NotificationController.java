package dev.sorokin.eventnotificator.api;

import dev.sorokin.eventnotificator.domain.NotificationService;
import dev.sorokin.eventnotificator.security.JwtValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final static Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    private final JwtValidationService jwtValidationService;

    private final NotificationService notificationService;

    private final NotificationResponseToDtoMapper notificationResponseToDtoMapper;

    public NotificationController(JwtValidationService jwtValidationService, NotificationService notificationService, NotificationResponseToDtoMapper notificationResponseToDtoMapper) {
        this.jwtValidationService = jwtValidationService;
        this.notificationService = notificationService;
        this.notificationResponseToDtoMapper = notificationResponseToDtoMapper;
    }

    @GetMapping
    public Long getNotifications(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        Long userId = jwtValidationService.extractUserId(token);

        // 5. Используем userId для получения уведомлений
        //    ВАЖНО: не проверяем существование этого userId в БД нотификатора,
        //    доверяем event-manager'у
//        return notificationService.getNotificationsByUserId(userId)
//                .stream()
//                .map(notificationResponseToDtoMapper::toDto)
//                .toList();

        return userId;
    }
}
