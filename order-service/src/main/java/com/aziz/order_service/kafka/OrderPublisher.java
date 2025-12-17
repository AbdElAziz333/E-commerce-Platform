package com.aziz.order_service.kafka;

import com.aziz.order_service.config.KafkaConfig;
import com.aziz.order_service.dto.OrderItemDto;
import com.aziz.order_service.kafka.events.OrderCreationEvent;
import com.aziz.order_service.kafka.events.OrderPaymentEvent;
import com.aziz.order_service.util.enums.OrderStatus;
import com.aziz.order_service.util.enums.PaymentMethod;
import com.aziz.order_service.util.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaConfig config;

    /**
     * publishes an event to the payment-service to continue payment business logic.
     * @author Aziz
     * */
    public void publishOrderPayment(
            String email,
            Long userId,
            UUID orderId,
            String orderNumber,
            OrderStatus orderStatus,
            BigDecimal totalAmount,
            PaymentMethod paymentMethod,
            PaymentStatus paymentStatus
    ) {
        OrderPaymentEvent event = new OrderPaymentEvent(
                userId,
                email,
                orderId,
                orderNumber,
                orderStatus,
                totalAmount,
                paymentMethod,
                paymentStatus
        );

        kafkaTemplate.send(config.getOrderCreated(), email, event);
    }

    /**
     * publishes an event to the notification-service to send notifications to user's mail.
     * @author Aziz
     * */
    public void publishOrder(
            String email,
            String orderNumber,
            OrderStatus orderStatus,
            BigDecimal shippingAmount,
            BigDecimal totalAmount,
            Long shippingAddressId,
            LocalDate estimatedDeliveryDate,
            List<OrderItemDto> orderItems
    ) {
        OrderCreationEvent event = new OrderCreationEvent(
                email,
                orderNumber,
                orderStatus,
                shippingAmount,
                totalAmount,
                shippingAddressId,
                estimatedDeliveryDate,
                orderItems
        );

        kafkaTemplate.send(config.getOrderPayment(), event.getEmail(), event);
    }
}