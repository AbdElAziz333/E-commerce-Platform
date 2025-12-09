package com.aziz.notification_service.events;

import com.aziz.notification_service.dto.OrderItemDto;
import com.aziz.notification_service.events.types.OrderEventType;
import com.aziz.notification_service.util.OrderStatus;
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