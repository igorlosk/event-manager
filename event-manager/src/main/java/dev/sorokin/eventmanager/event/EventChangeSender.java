package dev.sorokin.eventmanager.event;

import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventChangeSender {
    private final static Logger logger = LoggerFactory.getLogger(EventChangeSender.class);

    private final KafkaTemplate kafkaTemplate;

    public EventChangeSender(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendChanges(EventChangeKafkaMessage message) {
        logger.info("Sending event: event={}", message);
        var result = kafkaTemplate.send(
                "events-topic",
                message.messageId(),
                message
        );

        result.thenAccept(kafkaMessage -> {
            logger.info("Send successful");
        });
    }
}
