package dev.sorokin.eventnotificator;

import dev.sorokin.eventcommon.kafka.EventChangeKafkaMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class KafkaConfig {

    @Bean
    public ConsumerFactory<UUID, EventChangeKafkaMessage> consumerFactory() {
        Map<String, Object> configProperties = new HashMap<>();
        configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "notificator-group");
        configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);

        var factory =new DefaultKafkaConsumerFactory<UUID, EventChangeKafkaMessage>(configProperties);
        factory.setValueDeserializer(new JsonDeserializer<>(EventChangeKafkaMessage.class, false));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<UUID, EventChangeKafkaMessage> containerFactory(
            ConsumerFactory<UUID, EventChangeKafkaMessage> consumerFactory
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<UUID, EventChangeKafkaMessage>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
