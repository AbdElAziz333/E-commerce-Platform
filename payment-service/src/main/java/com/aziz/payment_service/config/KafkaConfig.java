package com.aziz.payment_service.config;

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
    private String paymentCompleted;
    private String paymentFailed;

    @Bean
    public NewTopic paymentCompletedEvent() {
        return new NewTopic(paymentCompleted, 3, (short) 1);
    }

    @Bean
    public NewTopic paymentFailedTopic() {
        return new NewTopic(paymentFailed, 3, (short) 1);
    }
}