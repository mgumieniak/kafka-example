package com.mgumieniak.kafkaexamples.kafka.api.producer;

import com.mgumieniak.kafkaexamples.AbstractKafkaTests;
import com.mgumieniak.kafkaexamples.KafkaTestTemplate;
import com.mgumieniak.kafkaexamples.domain.user.User;
import com.mgumieniak.kafkaexamples.kafka.api.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.mgumieniak.kafkaexamples.kafka.api.topic.TopicsConfig.USER_TOPIC_NAME;


@SpringBootTest
public class UserServiceTest extends AbstractKafkaTests {

    @Autowired
    private UserService userService;

    private static final KafkaTestTemplate<String, User> kafkaTestTemplate = new KafkaTestTemplate<>();

    @BeforeAll
    static void beforeAll() {
        kafkaTestTemplate.setUpAndStartContainer(USER_TOPIC_NAME, 3);

    }

    @AfterAll
    static void afterAll() {
        kafkaTestTemplate.stopContainer();
    }

    @Test
    public void pushesUserInTx() {
        User user1 = User.builder().firstName("Maciej").lastName("Gumieniak").uuid("1").build();
        User user2 = User.builder().firstName("Gela").lastName("Jeż").uuid("2").build();

        userService.sendRecordsToKafkaInTx(user1, user2);

        kafkaTestTemplate.assertThatContains(user1, user2);
    }

    @Test
    void doesNotSendRecordsToKafkaInTxDueToException() {
        User user1 = User.builder().firstName("Maciej").lastName("Gumieniak").uuid("1").build();
        User user2 = User.builder().firstName("Gela").lastName("Jeż").uuid("2").build();

        Assertions.assertThatThrownBy(() -> userService.doesNotSendRecordsToKafkaInTxDueToException(user1, user2));
        kafkaTestTemplate.assertThatDoesNotContain(user1, user2);
    }
}
