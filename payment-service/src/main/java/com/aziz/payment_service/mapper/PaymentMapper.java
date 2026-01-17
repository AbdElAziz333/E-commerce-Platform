package com.aziz.payment_service.mapper;

import com.aziz.payment_service.kafka.events.OrderPaymentEvent;
import com.aziz.payment_service.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public Payment orderEventToPayment(OrderPaymentEvent event) {
        return Payment.builder()
                .userId(event.getUserId())
                .orderId(event.getOrderId())
                .orderNumber(event.getOrderNumber())
                .totalAmount(event.getTotalAmount())
                .method(event.getPaymentMethod())
                .status(event.getPaymentStatus())
                .build();
    }
}