package com.aziz.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductDto {
    private String productId;

    private String name;

    private String description;

    private String shortDescription;

    private String sku;

    private String slug;

    private Double price;

    private Integer stockQuantity;
}