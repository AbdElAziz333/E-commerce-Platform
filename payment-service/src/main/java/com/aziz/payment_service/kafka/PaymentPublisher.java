package com.aziz.payment_service.kafka;

import com.aziz.payment_service.config.KafkaConfig;
import com.aziz.payment_service.kafka.events.PaymentFailedEvent;
import com.aziz.payment_service.kafka.events.PaymentSuccessEvent;
import com.aziz.payment_service.util.enums.PaymentMethod;
import com.aziz.payment_service.util.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaConfig config;

    /**
     * Publish payment success event to the notification-service
     * @author Aziz
     * */
    public void publishPaymentCompleted(String email, String orderNumber, PaymentMethod paymentMethod) {
        PaymentSuccessEvent event = new PaymentSuccessEvent(
                orderNumber,
                paymentMethod,
                PaymentStatus.SUCCESS
        );

        kafkaTemplate.send(config.getPaymentCompleted(), email, event);
    }

    /**
     * Publish payment failed event to the notification-service
     * @author Aziz
     * */
    public void publishPaymentFailed(String email, String orderNumber, PaymentMethod paymentMethod) {
        PaymentFailedEvent event = new PaymentFailedEvent(
                orderNumber,
                paymentMethod,
                PaymentStatus.FAILED
        );

        kafkaTemplate.send(config.getPaymentFailed(), email, event);
    }
}