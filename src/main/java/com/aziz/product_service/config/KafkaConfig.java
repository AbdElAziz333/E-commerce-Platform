package com.aziz.product_service.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Getter
@Setter
@EnableKafka
public class KafkaConfig {
    private String topic;

    @Bean
    public NewTopic kafkaTopic() {
        return new NewTopic(topic, 3, (short) 1);
    }
}