package com.aziz.payment_service.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
//    private Long orderItemId;
    private String productNameSnapshot;
//    private String skuSnapshot;
    private Integer quantity;
    private BigDecimal unitPrice;
//    private BigDecimal taxAmount;
    private BigDecimal totalPrice;

// TODO: we have to add variantAttributes
//    private List<String> variantAttributes;
//    private Integer returnedQuantity;

    private OrderDto order;
//    private String productId;
}