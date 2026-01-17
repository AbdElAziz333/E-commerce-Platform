package com.aziz.product_service.mapper;

import com.aziz.product_service.dto.ProductCreationRequest;
import com.aziz.product_service.dto.ProductDto;
import com.aziz.product_service.model.Product;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ProductMapper {
    public Product creationRequestToProduct(ProductCreationRequest request) {
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
                .userId(product.getUserId())
                .productId(product.getProductId())
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