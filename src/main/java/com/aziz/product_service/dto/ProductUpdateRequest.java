package com.aziz.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductUpdateRequest {
    private String productId;

    private String name;

    private String description;

    private String shortDescription;

    private String sku;

    private Double price;

    private Integer stockQuantity;
}