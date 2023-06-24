package com.mgumieniak.kafkaexamples.kafka.api.user;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Service
@RequiredArgsConstructor
public class ProducerConfig {

    private final KafkaProperties kafkaProperties;

    public Map<String, Object> txProducerConfigs() {
        Map<String, Object> props =
                new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(CLIENT_ID_CONFIG, "java-client-tx-producer");
        props.put(ENABLE_IDEMPOTENCE_CONFIG, "true");
        props.put(ACKS_CONFIG, "all");
        props.put(TRANSACTIONAL_ID_CONFIG, "java-client-tx-producer");
        return props;
    }
}
