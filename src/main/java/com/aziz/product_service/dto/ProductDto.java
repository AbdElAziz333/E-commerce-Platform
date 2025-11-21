package com.aziz.product_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class ProductDto {
    private Long userId;
    private String productId;

    private String name;

    private String description;

    private String shortDescription;

    private String sku;

    private String slug;

    private Double price;

    private Integer stockQuantity;
    private List<String> variantAttributes;
}