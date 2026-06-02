package dev.sorokin.eventnotificator.domain;

import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import dev.sorokin.eventnotificator.api.MarkNotificationsAsReadRequestDto;
import dev.sorokin.eventnotificator.db.NotificationEntity;
import dev.sorokin.eventnotificator.db.NotificationEntityRepository;
import dev.sorokin.eventnotificator.db.NotificationEventPayloadEntity;
import dev.sorokin.eventnotificator.db.NotificationEventPayloadEntityRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationEventPayloadEntityRepository notificationEventPayloadEntityRepository;

    private final NotificationEntityRepository notificationEntityRepository;

    private final NotificationPayloadMapper notificationPayloadMapper;

    private final DateTimeConverter dateTimeConverter;

    public NotificationService(
            NotificationEventPayloadEntityRepository notificationEventPayloadEntityRepository,
            NotificationEntityRepository notificationEntityRepository, NotificationPayloadMapper notificationPayloadMapper, DateTimeConverter dateTimeConverter) {
        this.notificationEventPayloadEntityRepository = notificationEventPayloadEntityRepository;
        this.notificationEntityRepository = notificationEntityRepository;
        this.notificationPayloadMapper = notificationPayloadMapper;
        this.dateTimeConverter = dateTimeConverter;
    }

    public void createNotificationForUsers(EventChangeKafkaMessage value) {
        NotificationEventPayloadEntity eventPayloadEntity =
                notificationEventPayloadEntityRepository.findByMessageId(value.messageId());

        value.subscribers().forEach(subscriberId -> {
                    notificationEntityRepository.save(new NotificationEntity(
                            null,
                            subscriberId,
                            false,
                            value.occurredAt(),
                            null,
                            eventPayloadEntity
                    ));
                    LOGGER.info("Created notification for user id={}", subscriberId);
                }
        );
    }

    public List<NotificationResponse> getNotificationsByUserId(Long userId) {

        List<NotificationEntity> list = notificationEntityRepository
                .findAllByUserId(userId)
                .stream()
                .toList();

        List<NotificationResponse> responses = list.stream()
                .map(notificationEntity -> new NotificationResponse(
                        notificationEntity.getId(),
                        notificationEntity.getPayload().getEventType(),
                        notificationEntity.getPayload().getEventId(),
                        dateTimeConverter.formatToString(notificationEntity.getCreatedAt()),
                        notificationEntity.isRead(),
                        "Событие было изменено",
                        notificationPayloadMapper.toNotificationPayload(notificationEntity.getPayload())
                ))
                .toList();

        return responses;
    }

    public void markAsRead(
            Long userId,
            MarkNotificationsAsReadRequestDto markNotificationsAsReadRequestDto) {

        List<Long> notificationsId = markNotificationsAsReadRequestDto.notificationIds();

        if (notificationsId == null || notificationsId.isEmpty()) {
            throw new IllegalArgumentException("No Ids to update");
        }

        int updatedCount = notificationEntityRepository
                .markAsReadByIdsAndUserId(notificationsId, userId);

        if (updatedCount == 0) {
            throw new IllegalArgumentException("No Ids to update");
        }

        LOGGER.info("Marked as read {} notifications for user {}", updatedCount, userId);
    }

    @Scheduled(cron = "${event.status.cron}")
    @Transactional
    public void deleteNotificationsOlderThan7Days() {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(7);
        int deletedNotifications = notificationEntityRepository.deleteNotificationsByPayloadDate(thresholdDate);
        LOGGER.info("Deleted {} notifications older than 7 days", deletedNotifications);
        int deletedNotificationsPayload = notificationEventPayloadEntityRepository.deletePayload(thresholdDate);
        LOGGER.info("Deleted {} notificationsPayload older than 7 days", deletedNotificationsPayload);
    }

}


