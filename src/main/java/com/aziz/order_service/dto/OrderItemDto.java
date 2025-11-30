package com.aziz.order_service.dto;

import com.aziz.order_service.model.Order;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long orderItemId;
    private String productNameSnapshot;
    private String skuSnapshot;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal taxAmount;
    private BigDecimal totalPrice;

//
//    private List<String> variantAttributes;
//    private Integer returnedQuantity;

    private Order order;
    private String productId;
}