package com.mgumieniak.kafkaexamples.kafka.api.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicsConfig {

    public static final String USER_TOPIC_NAME = "USER";
    public static final int USER_TOPIC_PARTITION_NO = 3;

    public static final String USER_VALIDATED_TOPIC_NAME = "USER_VALIDATED";
    public static final int USER_VALIDATED_TOPIC_PARTITION_NO = 3;

    @Bean
    public NewTopic userTopic() {
        return TopicBuilder.name(USER_TOPIC_NAME)
                .partitions(USER_TOPIC_PARTITION_NO)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userValidatedTopic() {
        return TopicBuilder.name(USER_VALIDATED_TOPIC_NAME)
                .partitions(USER_VALIDATED_TOPIC_PARTITION_NO)
                .replicas(1)
                .build();
    }
}
