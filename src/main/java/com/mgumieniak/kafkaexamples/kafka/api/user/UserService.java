package com.mgumieniak.kafkaexamples.kafka.api.user;

import com.mgumieniak.kafkaexamples.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mgumieniak.kafkaexamples.kafka.api.topic.TopicsConfig.USER_TOPIC_NAME;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KafkaTemplate<String, User> template;

    @Transactional
    public void sendRecordToKafka(User user){
        template.send(USER_TOPIC_NAME, user.getUuid(), user);
    }

    @Transactional
    public void sendRecordsToKafkaInTx(User... user){
        for (User u : user) {
            template.send(USER_TOPIC_NAME, u.getUuid(), u);
        }
    }
    @Transactional
    public void doesNotSendRecordsToKafkaInTxDueToException(User... user){
        for (User u : user) {
            template.send(USER_TOPIC_NAME, u.getUuid(), u);
        }
        throw new IllegalArgumentException("Abort tx");
    }
}
