package com.mgumieniak.kafkaexamples.kafka.api.user;

import com.mgumieniak.kafkaexamples.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;

@Configuration
@RequiredArgsConstructor
public class UserTemplateConfig {
    private final ProducerConfig producerConfig;

    @Bean
    public ProducerFactory<String, User> userTxProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig.txProducerConfigs());
    }

    @Bean
    public KafkaTemplate<String, User> userTxKafkaTemplate() {
        return new KafkaTemplate<>(userTxProducerFactory());
    }

    @Bean
    public KafkaTransactionManager<String, User> kafkaTransactionManager() {
        return new KafkaTransactionManager<>(userTxProducerFactory());
    }
}
