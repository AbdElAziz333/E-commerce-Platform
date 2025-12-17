package com.aziz.notification_service.kafka.events;

import com.aziz.notification_service.dto.OrderItemDto;
import com.aziz.notification_service.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationEvent {
    private String email;
    private String orderNumber;
    private OrderStatus orderStatus;
    private BigDecimal shippingAmount;
    private BigDecimal totalAmount;
    private Long shippingAddressId;
    private LocalDate estimatedDeliveryDate;
    private List<OrderItemDto> orderItems;
}