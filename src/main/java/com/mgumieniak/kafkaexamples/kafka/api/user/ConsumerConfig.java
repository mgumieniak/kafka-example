package com.mgumieniak.kafkaexamples.kafka.api.user;

import com.mgumieniak.kafkaexamples.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE;
import static org.springframework.kafka.support.LogIfLevelEnabled.Level.INFO;

@Configuration
@RequiredArgsConstructor
public class ConsumerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(
                kafkaProperties.buildConsumerProperties()
        );
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(CLIENT_ID_CONFIG, "tpd-loggers-client");
        props.put(GROUP_ID_CONFIG, "tpd-loggers");
        props.put(ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");

        return props;
    }

    @Bean
    public ConsumerFactory<String, User> txConsumerFactory() {
        final JsonDeserializer<User> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("com.mgumieniak.*");
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs(), new StringDeserializer(), jsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, User> txKafkaListenerContainerFactory(
            KafkaTransactionManager<String, Object> kafkaTransactionManager
    ) {
        ConcurrentKafkaListenerContainerFactory<String, User> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(txConsumerFactory());
        factory.setConcurrency(1);
        factory.getContainerProperties().setSyncCommits(false);
        factory.getContainerProperties().setAckMode(MANUAL_IMMEDIATE);
        factory.getContainerProperties().setCommitLogLevel(INFO);
        factory.getContainerProperties().setTransactionManager(kafkaTransactionManager);
        return factory;
    }
}
