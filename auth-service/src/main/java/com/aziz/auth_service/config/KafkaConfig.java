package com.aziz.auth_service.config;

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
    private String otpVerification;
    private String userRegistered;

    @Bean
    public NewTopic otpVerificationTopic() {
        return new NewTopic(otpVerification, 3, (short) 1);
    }

    @Bean
    public NewTopic userRegisteredTopic() {
        return new NewTopic(userRegistered, 3, (short) 1);
    }
}