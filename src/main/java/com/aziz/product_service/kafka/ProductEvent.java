package com.aziz.product_service.kafka;

import com.aziz.product_service.util.ProductEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEvent {
    private ProductEventType type;
    private String productId;
    private String sku;
    private String slug;
    private String name;
    private String description;
    private String shortDescription;
    private Double price;
    private Integer stockQuantity;
}