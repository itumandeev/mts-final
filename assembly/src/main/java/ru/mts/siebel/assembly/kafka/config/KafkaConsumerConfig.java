package ru.mts.siebel.assembly.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.mts.siebel.model.AssemblyMessage;

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
    public ConsumerFactory<String, AssemblyMessage> assemblyMessageConsumerFactory() {
        final ErrorHandlingDeserializer<AssemblyMessage> errorHandlingDeserializer =
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(AssemblyMessage.class, false));
        return new DefaultKafkaConsumerFactory<>(this.consumerConfigs(), new StringDeserializer(), errorHandlingDeserializer);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AssemblyMessage> assemblyMessageKafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, AssemblyMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.assemblyMessageConsumerFactory());
        return factory;
    }


}
