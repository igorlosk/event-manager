package dev.sorokin.eventnotificator;

import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventKafkaListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventKafkaListener.class);

    @KafkaListener(topics = "events-topic", containerFactory = "containerFactory")
    public void listenEvents(
            ConsumerRecord<Field.UUID, EventChangeKafkaMessage> record
    ){
        LOGGER.info("get book event: event={}", record.value());
    }
}
