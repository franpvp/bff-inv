package com.example.bff_inventario.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaProducerHolder {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Producer<String, String> PRODUCER;

    private static String bootstrapServers() {
        String v = System.getenv("KAFKA_BOOTSTRAP");
        if (v == null || v.isBlank()) v = System.getenv("SPRING_KAFKA_BOOTSTRAP_SERVERS");
        if (v == null || v.isBlank()) v = "kafka:9092";
        return v;
    }


    private static void copySecurity(Properties props) {
        String protocol = System.getenv("KAFKA_SECURITY_PROTOCOL");
        if (protocol != null && !protocol.isBlank()) {
            props.put("security.protocol", protocol);
            String mech = System.getenv("KAFKA_SASL_MECHANISM");
            String jaas = System.getenv("KAFKA_SASL_JAAS");
            if (mech != null) props.put("sasl.mechanism", mech);
            if (jaas != null) props.put("sasl.jaas.config", jaas);
        }
    }

    static {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        props.put(ProducerConfig.RETRIES_CONFIG, 10);

        PRODUCER = new KafkaProducer<>(props);

        try {
            KafkaTopicInitializer.createTopicIfNotExists(bootstrapServers(), "productos.v1", props);
            KafkaTopicInitializer.createTopicIfNotExists(bootstrapServers(), "bodegas.v1", props);
            System.out.println("[Kafka] topicos verificados/creados");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private KafkaProducerHolder() {}

    public static void send(String topic, String key, Object event) throws Exception {
        String payload = MAPPER.writeValueAsString(event);
        PRODUCER.send(new ProducerRecord<>(topic, key, payload), (md, ex) -> {
            if (ex != null) ex.printStackTrace();
        });
    }
}