package com.mgumieniak.kafkaexamples.kafka.api.user;

import com.mgumieniak.kafkaexamples.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mgumieniak.kafkaexamples.kafka.api.topic.TopicsConfig.USER_TOPIC_NAME;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final UserValidatedService userValidatedService;

    @Transactional
    @KafkaListener(topics = USER_TOPIC_NAME, containerFactory = "kafkaListenerContainerFactory")
    public void listenAsObject(User user, Acknowledgment ack) {
        userValidatedService.validateUser(user);
        ack.acknowledge();
    }
}
