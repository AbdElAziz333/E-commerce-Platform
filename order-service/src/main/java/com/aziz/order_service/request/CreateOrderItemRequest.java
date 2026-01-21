package com.aziz.order_service.request;

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
public class CreateOrderItemRequest {
    @NotNull(message = "Product name is required")
    private String productNameSnapshot;

    @NotNull(message = "SKU is required")
    private String skuSnapshot;

    @Min(1)
    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @DecimalMin(value = "0.01")
    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice;
}