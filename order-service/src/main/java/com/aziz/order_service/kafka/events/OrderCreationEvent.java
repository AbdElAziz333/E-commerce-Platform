package com.aziz.order_service.kafka.events;

import com.aziz.order_service.dto.OrderItemDto;
import com.aziz.order_service.util.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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
    private List<OrderItemDto> orderItems;
}