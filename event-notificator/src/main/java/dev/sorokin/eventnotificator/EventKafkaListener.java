package dev.sorokin.eventnotificator;

import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import dev.sorokin.eventnotificator.domain.NotificationEventPayloadService;
import dev.sorokin.eventnotificator.domain.NotificationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventKafkaListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventKafkaListener.class);

    private final NotificationEventPayloadService notificationEventPayloadService;

    private final NotificationService notificationService;

    public EventKafkaListener(NotificationEventPayloadService notificationEventPayloadService, NotificationService notificationService) {
        this.notificationEventPayloadService = notificationEventPayloadService;
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "events-topic", containerFactory = "containerFactory")
    public void listenEvents(
            ConsumerRecord<UUID, EventChangeKafkaMessage> record
    ) {
        LOGGER.info("Event changed: event={}", record.value());
        notificationEventPayloadService.saveNotifications(record.value());
        notificationService.createNotificationForUsers(record.value());

    }
}
