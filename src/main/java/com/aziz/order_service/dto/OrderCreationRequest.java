package com.aziz.order_service.dto;

import com.aziz.order_service.util.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationRequest {
    private List<OrderItemCreationRequest> orderItems;
    private BigDecimal shippingAmount;
    private BigDecimal totalAmount;
    private String notes;
    private Long shippingAddressId;
    private PaymentMethod paymentMethod;
}