package com.aziz.order_service.config;

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
    private String orderCreated;
    private String orderPayment;

    public NewTopic orderCreatedTopic() {
        return new NewTopic(orderCreated, 3, (short) 1);
    }

    @Bean
    public NewTopic orderPaymentTopic() {
        return new NewTopic(orderPayment, 3, (short) 1);
    }
}