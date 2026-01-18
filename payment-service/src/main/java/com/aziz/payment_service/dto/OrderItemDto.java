package com.aziz.payment_service.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private String productNameSnapshot;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}