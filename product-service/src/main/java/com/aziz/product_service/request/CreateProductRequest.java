package com.aziz.product_service.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 10000)
    private String description;

    @NotBlank
    @Size(max = 1000)
    private String shortDescription;

    @NotBlank
    private String sku;

    @NotNull
    private Double price;

    @NotNull
    private Integer stockQuantity;
//    private List<String> variantAttributes;
}