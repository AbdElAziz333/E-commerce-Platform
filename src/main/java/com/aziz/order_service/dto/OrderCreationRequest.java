package com.aziz.order_service.dto;

import com.aziz.order_service.model.OrderItem;
import com.aziz.order_service.util.enums.PaymentMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderCreationRequest {
    private List<OrderItem> orderItems;
    private BigDecimal shippingAmount;
    private BigDecimal totalAmount;

    private String notes;

    private Long userId;
    private Long shippingAddressId;

    private Long transactionId;
    private PaymentMethod paymentMethod;
}