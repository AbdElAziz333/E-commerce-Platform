package com.aziz.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductCreationRequest {
    private String name;
    private String description;
    private String shortDescription;
    private String sku;
    private Double price;
    private Integer stockQuantity;
    private List<String> variantAttributes;
}