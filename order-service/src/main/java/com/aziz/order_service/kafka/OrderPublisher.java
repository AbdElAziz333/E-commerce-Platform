package com.aziz.order_service.kafka;

import com.aziz.order_service.config.KafkaConfig;
import com.aziz.order_service.kafka.events.OrderCreationEvent;
import com.aziz.order_service.kafka.events.OrderPaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaConfig config;

    /**
     * publishes an event to the notification-service to send notifications to user's mail.
     * @author Aziz
     * */
    public void publish(OrderCreationEvent event) {
        send(config.getOrderCreated(), event.getEmail(), event);
    }

    /**
     * publishes an event to the payment-service to continue payment business logic.
     * @author Aziz
     * */
    public void publish(OrderPaymentEvent event) {
        send(config.getOrderPayment(), event.getEmail(), event);
    }

    private void send(String topic, String key, Object event) {
        kafkaTemplate.send(topic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish to topic {} event {}", topic, event, ex);
                    } else {
                        log.debug("Published to topic {} offset={}", topic, result.getRecordMetadata().offset());
                    }
                });
    }
}