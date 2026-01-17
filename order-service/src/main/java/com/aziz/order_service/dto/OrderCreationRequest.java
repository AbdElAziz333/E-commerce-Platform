package com.aziz.order_service.dto;

import com.aziz.order_service.util.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationRequest {
    @NotNull
    @Valid
    private List<OrderItemCreationRequest> orderItems;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal shippingAmount;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal totalAmount;

    @NotNull
    private String notes;

    @NotNull
    private Long shippingAddressId;

    @NotNull
    private PaymentMethod paymentMethod;
}