package com.aziz.order_service.dto;

import com.aziz.order_service.util.enums.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private UUID id;
    private String orderNumber;
    private OrderStatus orderStatus;
    private BigDecimal shippingAmount;
    private BigDecimal totalAmount;
    private Long shippingAddressId;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String notes;
    private List<OrderItemDto> items;
}