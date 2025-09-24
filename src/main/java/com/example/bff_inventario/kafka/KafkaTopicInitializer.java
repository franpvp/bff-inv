package com.example.bff_inventario.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class KafkaTopicInitializer {

    public static void createTopicIfNotExists(String bootstrapServers, String topicName, Properties baseProps) throws Exception {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // Copia seguridad si existiera en baseProps
        for (String k : List.of("security.protocol","sasl.mechanism","sasl.jaas.config")) {
            Object v = baseProps.get(k);
            if (v != null) props.put(k, v);
        }

        int attempts = 0;
        Exception last = null;
        while (attempts < 5) {
            attempts++;
            try (AdminClient admin = AdminClient.create(props)) {
                Set<String> topics = admin.listTopics().names().get(10, java.util.concurrent.TimeUnit.SECONDS);
                if (!topics.contains(topicName)) {
                    NewTopic topic = new NewTopic(topicName, 1, (short) 1);
                    admin.createTopics(Collections.singleton(topic)).all().get(10, java.util.concurrent.TimeUnit.SECONDS);
                    System.out.println("[KafkaTopicInitializer] Tópico creado: " + topicName);
                } else {
                    System.out.println("[KafkaTopicInitializer] Tópico ya existe: " + topicName);
                }
                return;
            } catch (Exception e) {
                last = e;
                System.out.println("[KafkaTopicInitializer] intento " + attempts + " falló: " + e.getMessage());
                Thread.sleep(2000);
            }
        }
        if (last != null) throw last;
    }
}
