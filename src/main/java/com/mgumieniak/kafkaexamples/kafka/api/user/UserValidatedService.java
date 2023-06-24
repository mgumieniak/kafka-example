package com.mgumieniak.kafkaexamples.kafka.api.user;

import com.mgumieniak.kafkaexamples.domain.user.User;
import com.mgumieniak.kafkaexamples.domain.user.UserValidated;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.mgumieniak.kafkaexamples.kafka.api.topic.TopicsConfig.USER_VALIDATED_TOPIC_NAME;

@Service
@RequiredArgsConstructor
public class UserValidatedService {

    private final KafkaTemplate<String, UserValidated> template;

    @Transactional
    public void validateUser(User user) {
        val userValidated = UserValidated.builder().user(user).valid(false).build();
        template.send(USER_VALIDATED_TOPIC_NAME, user.getUuid(), userValidated);
    }
}
