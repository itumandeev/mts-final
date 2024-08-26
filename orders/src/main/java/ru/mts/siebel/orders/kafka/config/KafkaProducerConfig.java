package ru.mts.siebel.orders.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.mts.siebel.model.OrderMessage;
import ru.mts.siebel.model.StartMessage;
import ru.mts.siebel.model.StatusMessage;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, OrderMessage> orderMessageKafkaTemplate() {
        return new KafkaTemplate<>(orderMessageProducerFactory());
    }

    @Bean
    public ProducerFactory<String, OrderMessage> orderMessageProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getConfig());
    }

    private Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return config;
    }

    @Bean
    public KafkaTemplate<String, StartMessage> startMessageKafkaTemplate() {
        return new KafkaTemplate<>(startMessageProducerFactory());
    }

    @Bean
    public ProducerFactory<String, StartMessage> startMessageProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getConfig());
    }

    @Bean
    public KafkaTemplate<String, StatusMessage> statusMessageKafkaTemplate() {
        return new KafkaTemplate<>(statusMessageProducerFactory());
    }

    @Bean
    public ProducerFactory<String, StatusMessage> statusMessageProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getConfig());
    }


}
