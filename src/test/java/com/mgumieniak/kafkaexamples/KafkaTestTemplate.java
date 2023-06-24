package com.mgumieniak.kafkaexamples;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.mgumieniak.kafkaexamples.AbstractKafkaTests.kafkaContainer;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES;

@Getter
public class KafkaTestTemplate<K, V> {
    private final BlockingQueue<ConsumerRecord<K, V>> records = new LinkedBlockingQueue<>();
    private KafkaMessageListenerContainer<K, V> container;

    public Map<String, Object> defaultConsumerProperties() {
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps(kafkaContainer.getBootstrapServers(), "test-consumer-group", "true");
        consumerProperties.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProperties.put(TRUSTED_PACKAGES, "com.mgumieniak.*");
        return consumerProperties;
    }

    public void setUpAndStartContainer(String topic, int topicNoPartition) {
        container = new KafkaMessageListenerContainer<>(
                new DefaultKafkaConsumerFactory<K, V>(defaultConsumerProperties()),
                new ContainerProperties(topic)
        );
        container.setupMessageListener((MessageListener<K, V>) records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, topicNoPartition);
    }

    public void stopContainer() {
        container.stop();
    }

    @SneakyThrows
    public ConsumerRecord<K, V> poll() {
        return records.poll(100, MILLISECONDS);
    }

    public final void assertThatContains(V... v) {
        val dest = new LinkedList<V>();
        for (int i = 0; i < v.length; i++) {
            ConsumerRecord<K, V> record = poll();
            if (record != null) {
                dest.add(record.value());
            }
        }
        assertThat(dest).containsExactlyInAnyOrder(v);
    }

    public final void assertThatDoesNotContain(V... v) {
        val dest = new LinkedList<V>();
        for (int i = 0; i < v.length; i++) {
            ConsumerRecord<K, V> record = poll();
            if (record != null) {
                dest.add(record.value());
            }
        }
        assertThat(dest).doesNotContainAnyElementsOf(Arrays.asList(v));
    }
}
