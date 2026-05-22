package dev.sorokin.eventnotificator.api;

import dev.sorokin.eventnotificator.domain.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/notifications")
public class NotificationController {

    private final static Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    private final NotificationResponseToDtoMapper notificationResponseToDtoMapper;

    public NotificationController(
            NotificationService notificationService,
            NotificationResponseToDtoMapper notificationResponseToDtoMapper
    ) {
        this.notificationService = notificationService;
        this.notificationResponseToDtoMapper = notificationResponseToDtoMapper;

    }

    @GetMapping
    public List<NotificationResponseDto> getNotifications(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        return notificationService.getAllNotifications()
                .stream()
                .map(notificationResponseToDtoMapper::toDto)
                .toList();
    }


}
