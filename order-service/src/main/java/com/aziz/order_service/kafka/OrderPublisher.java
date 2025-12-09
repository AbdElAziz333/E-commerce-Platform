package com.aziz.order_service.kafka;

import com.aziz.order_service.config.KafkaConfig;
import com.aziz.order_service.dto.OrderItemDto;
import com.aziz.order_service.events.OrderEvent;
import com.aziz.order_service.events.OrderEventType;
import com.aziz.order_service.util.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderPublisher {
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final KafkaConfig config;

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
        OrderEvent event = new OrderEvent(
                OrderEventType.ORDER_CREATED,
                email,
                orderNumber,
                orderStatus,
                shippingAmount,
                totalAmount,
                shippingAddressId,
                estimatedDeliveryDate,
                orderItems
        );
        kafkaTemplate.send(config.getOrderEvents(), event.getEmail(), event);
    }
}