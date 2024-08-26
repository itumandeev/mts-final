package ru.mts.siebel.orders.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.mts.siebel.model.OrderMessage;
import ru.mts.siebel.model.StartMessage;
import ru.mts.siebel.model.StatusMessage;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {


    private Map<String, Object> consumerConfigs() {
        final Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumeer-group");
        return props;
    }

    @Bean
    public ConsumerFactory<String, OrderMessage> orderMessageConsumerFactory() {
        final ErrorHandlingDeserializer<OrderMessage> errorHandlingDeserializer =
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(OrderMessage.class, false));
        return new DefaultKafkaConsumerFactory<>(this.consumerConfigs(), new StringDeserializer(), errorHandlingDeserializer);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderMessage> orderMessageKafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, OrderMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.orderMessageConsumerFactory());
        return factory;
    }
    /**/
    @Bean
    public ConsumerFactory<String, StartMessage> startMessageConsumerFactory() {
        final ErrorHandlingDeserializer<StartMessage> errorHandlingDeserializer =
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(StartMessage.class, false));
        return new DefaultKafkaConsumerFactory<>(this.consumerConfigs(), new StringDeserializer(), errorHandlingDeserializer);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StartMessage> startMessageKafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, StartMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.startMessageConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, StatusMessage> statusMessageConsumerFactory() {
        final ErrorHandlingDeserializer<StatusMessage> errorHandlingDeserializer =
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(StatusMessage.class, false));
        return new DefaultKafkaConsumerFactory<>(this.consumerConfigs(), new StringDeserializer(), errorHandlingDeserializer);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StatusMessage> statusMessageKafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, StatusMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.statusMessageConsumerFactory());
        return factory;
    }

}
