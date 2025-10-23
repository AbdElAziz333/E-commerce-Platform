package com.aziz.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class ProductRegisterRequest {

    private String name;

    private String description;

    private String shortDescription;

    private String sku;

    private String slug;

    private Double price;

    private Integer stockQuantity;
}