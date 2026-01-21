package com.aziz.order_service.request;

import com.aziz.order_service.util.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @NotNull(message = "Shipping address ID is required")
    private Long shippingAddressId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Shipping amount is required")
    @DecimalMin(value = "0.01")
    private BigDecimal shippingAmount;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

    @Valid
    @NotEmpty(message = "Order must contain at lease one item")
    private List<CreateOrderItemRequest> items;
}