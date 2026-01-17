package com.aziz.payment_service.dto;

import com.aziz.payment_service.util.enums.OrderStatus;
import com.aziz.payment_service.util.enums.PaymentMethod;
import com.aziz.payment_service.util.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderDto {
    private UUID orderId;
    private String orderNumber;
    private OrderStatus orderStatus;
    private BigDecimal shippingAmount;
    private BigDecimal totalAmount;
    private Long shippingAddressId;
    private LocalDate estimatedDeliveryDate;
    private LocalDate deliveredAt;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;

    private String notes;
    private List<OrderItemDto> orderItems;
}