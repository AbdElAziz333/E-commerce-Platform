package com.aziz.product_service.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
    private String name;

    @Size(max = 10000)
    private String description;

    @Size(max = 1000)
    private String shortDescription;

    private String sku;

    private Double price;

    private Integer stockQuantity;
}