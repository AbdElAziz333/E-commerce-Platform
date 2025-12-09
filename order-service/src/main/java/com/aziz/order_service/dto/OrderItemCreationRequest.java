package com.aziz.order_service.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// TODO: add texAmount and variants in the future..
public class OrderItemCreationRequest {
    private String productNameSnapshot;
    private String skuSnapshot;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}