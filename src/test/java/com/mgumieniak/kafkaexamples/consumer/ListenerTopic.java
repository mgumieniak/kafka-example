package com.mgumieniak.kafkaexamples.consumer;

import com.mgumieniak.kafkaexamples.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import static com.mgumieniak.kafkaexamples.kafka.api.topic.TopicsConfig.USER_TOPIC_NAME;

@Slf4j
@TestConfiguration
public class ListenerTopic {


    @KafkaListener(topics = USER_TOPIC_NAME)
    public void listenAsObject(User user, Acknowledgment ack) {
        log.info("Received advice-topic: " + user);

        ack.acknowledge();
    }
}

