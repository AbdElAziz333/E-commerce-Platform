package com.aziz.order_service.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private String productId;
    private String productNameSnapshot;
    private String productSlug;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}