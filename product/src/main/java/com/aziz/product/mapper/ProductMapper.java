package com.aziz.product.mapper;

import com.aziz.product.dto.request.CreateProductRequest;
import com.aziz.product.dto.response.ProductDto;
import com.aziz.product.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product creationRequestToProduct(CreateProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .shortDescription(request.getShortDescription())
                .sku(request.getSku())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
//                .variantAttributes(request.getVariantAttributes())
                .build();
    }

    public ProductDto productToDto(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .shortDescription(product.getShortDescription())
                .sku(product.getSku())
                .slug(product.getSlug())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
//                .variantAttributes(product.getVariantAttributes())
                .build();
    }
}