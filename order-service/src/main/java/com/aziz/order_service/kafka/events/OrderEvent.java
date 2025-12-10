package com.aziz.order_service.kafka.events;

import com.aziz.order_service.dto.OrderItemDto;
import com.aziz.order_service.kafka.events.types.OrderEventType;
import com.aziz.order_service.util.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private OrderEventType type;
    private String email;
    private String orderNumber;
    private OrderStatus orderStatus;
    private BigDecimal shippingAmount;
    private BigDecimal totalAmount;
    private Long shippingAddressId;
    private LocalDate estimatedDeliveryDate;
    private List<OrderItemDto> orderItems;
}