package dev.sorokin.eventmanager.web;

import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaTemplate<Field.UUID, EventChangeKafkaMessage> kafkaTemplate(
            KafkaProperties kafkaProperties
    ) {
        var properties = kafkaProperties.buildProducerProperties();
        ProducerFactory<Field.UUID, EventChangeKafkaMessage> producerFactory = new DefaultKafkaProducerFactory<>(properties);
        return new KafkaTemplate<>(producerFactory);
    }
}
