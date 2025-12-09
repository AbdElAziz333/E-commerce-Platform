package com.aziz.user_service.config;

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
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfig {
    private String userEvents;

    @Bean
    public NewTopic newTopic() {
        return new NewTopic(userEvents, 3, (short) 1);
    }
}