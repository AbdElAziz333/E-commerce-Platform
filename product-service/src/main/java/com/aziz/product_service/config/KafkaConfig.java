package com.aziz.product_service.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Getter
@Setter
@EnableKafka
@Configuration
@ConfigurationProperties(prefix = "kafka.topic")
public class KafkaConfig {
    private String productCreated;

    @Bean
    public NewTopic productCreatedTopic() {
        return new NewTopic(productCreated, 3, (short) 1);
    }
}