package dev.sorokin.eventnotificator.api;

import dev.sorokin.eventnotificator.domain.NotificationService;
import dev.sorokin.eventnotificator.security.JwtValidationService;
import dev.sorokin.eventnotificator.web.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public List<NotificationResponseDto> getNotifications(
            @RequestHeader("Authorization") String authHeader) {

        isHeaderPresent(authHeader);

        String token = authHeader.substring(7);

        validToken(token);

        try {
            Long userId = jwtValidationService.extractUserId(token);
            return notificationService.getNotificationsByUserId(userId)
                    .stream()
                    .map(notificationResponseToDtoMapper::toDto)
                    .toList();
        } catch (Exception e) {
            LOGGER.warn("Invalid token used: {}", e.getMessage());
            throw new UnauthorizedException("Invalid token");
        }

    }


    @PostMapping
    public ResponseEntity<Void> markAsRead(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody MarkNotificationsAsReadRequestDto markNotificationsAsReadRequestDto) {

        isHeaderPresent(authHeader);

        String token = authHeader.substring(7);

        validToken(token);

        try {
            Long userId = jwtValidationService.extractUserId(token);
            notificationService.markAsRead(userId, markNotificationsAsReadRequestDto);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            LOGGER.warn("Invalid token used: {}", e.getMessage());
            throw new UnauthorizedException("Invalid token");
        }
    }

    private static void validToken(String token) {
        if (token.isBlank()) {
            LOGGER.warn("Empty token provided");
            throw new UnauthorizedException("Token is empty");
        }
    }

    private static void isHeaderPresent(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }
    }
}
