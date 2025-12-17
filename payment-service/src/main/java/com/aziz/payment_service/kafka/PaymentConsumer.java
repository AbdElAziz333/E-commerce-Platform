package com.aziz.payment_service.kafka;

import com.aziz.payment_service.kafka.events.PaymentEvent;
import com.aziz.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {
    private final PaymentService service;

    @KafkaListener(topics = "${kafka.topic.order-payment}")
    public void consumeOrderPayment(PaymentEvent event) {
            service.sendAndSavePayment(event);
    }
}