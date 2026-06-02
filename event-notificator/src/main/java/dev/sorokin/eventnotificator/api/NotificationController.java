package dev.sorokin.eventnotificator.api;

import dev.sorokin.eventnotificator.domain.NotificationService;
import dev.sorokin.eventnotificator.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    private final NotificationResponseToDtoMapper notificationResponseToDtoMapper;

    private final AuthenticationService authenticationService;

    public NotificationController(
            NotificationService notificationService,
            NotificationResponseToDtoMapper notificationResponseToDtoMapper,
            AuthenticationService authenticationService) {
        this.notificationService = notificationService;
        this.notificationResponseToDtoMapper = notificationResponseToDtoMapper;
        this.authenticationService = authenticationService;
    }


    @GetMapping
    public List<NotificationResponseDto> getNotifications() {
        var authUser = authenticationService.getAuthUser();
        return notificationService.getNotificationsByUserId(authUser.id())
                .stream()
                .map(notificationResponseToDtoMapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<Void> markAsRead (
            @RequestBody MarkNotificationsAsReadRequestDto markNotificationsAsReadRequestDto
    ){
        var authUser = authenticationService.getAuthUser();
        notificationService.markAsRead(authUser.id(), markNotificationsAsReadRequestDto);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
