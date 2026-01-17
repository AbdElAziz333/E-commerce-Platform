package com.aziz.order_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemCreationRequest {
    @NotNull
    private String productNameSnapshot;

    @NotNull
    private String skuSnapshot;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal unitPrice;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal totalPrice;
}