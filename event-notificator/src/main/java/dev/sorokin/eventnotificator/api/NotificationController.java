package dev.sorokin.eventnotificator.api;

import dev.sorokin.eventnotificator.domain.NotificationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    private final NotificationResponseToDtoMapper notificationResponseToDtoMapper;

    public NotificationController(NotificationService notificationService, NotificationResponseToDtoMapper notificationResponseToDtoMapper) {
        this.notificationService = notificationService;
        this.notificationResponseToDtoMapper = notificationResponseToDtoMapper;
    }

    @GetMapping
    public List<NotificationResponseDto> getNotifications(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String jwtToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        }

        System.out.println("JWT Token: " + jwtToken);
        return notificationService.getAllNotifications()
                .stream()
                .map(notificationResponseToDtoMapper::toDto)
                .toList();
    }

}
