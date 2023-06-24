package com.mgumieniak.kafkaexamples.kafka.api.user;

import com.mgumieniak.kafkaexamples.domain.user.UserValidated;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;

@Configuration
@RequiredArgsConstructor
public class UserValidatedTemplateConfig {
    private final ProducerConfig producerConfig;

    @Bean
    public ProducerFactory<String, UserValidated> userValidatedTxProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig.txProducerConfigs());
    }

    @Bean
    public KafkaTemplate<String, UserValidated> userValidatedTxKafkaTemplate() {
        return new KafkaTemplate<>(userValidatedTxProducerFactory());
    }
}
