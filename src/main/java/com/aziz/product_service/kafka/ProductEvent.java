package com.aziz.product_service.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEvent {
    private String productId;
    private String sku;
    private String slug;
    private String name;
    private String description;
    private String shortDescription;
    private Double price;
    private Integer stockQuantity;
}