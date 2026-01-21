package com.aziz.product_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String name;
    private String description;
    private String shortDescription;
    private String sku;
    private String slug;
    private Double price;
    private Integer stockQuantity;
//    private List<String> variantAttributes;
}