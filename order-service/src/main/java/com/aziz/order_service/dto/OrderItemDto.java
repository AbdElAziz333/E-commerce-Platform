package com.aziz.order_service.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long id;
    private String productNameSnapshot;
    private String skuSnapshot;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}