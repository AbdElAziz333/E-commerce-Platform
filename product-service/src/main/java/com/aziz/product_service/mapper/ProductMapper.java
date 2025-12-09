package com.aziz.product_service.mapper;

import com.aziz.product_service.dto.ProductCreationRequest;
import com.aziz.product_service.dto.ProductDto;
import com.aziz.product_service.kafka.ProductEvent;
import com.aziz.product_service.model.Product;
import com.aziz.product_service.model.ProductSearch;
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

    public ProductEvent productToEvent(Product product) {
        return ProductEvent.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .shortDescription(product.getShortDescription())
                .sku(product.getSku())
                .slug(product.getSlug())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
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

    public ProductSearch eventToProductSearch(ProductEvent event) {
        return ProductSearch.builder()
                .productId(event.getProductId())
                .name(event.getName())
                .description(event.getDescription())
                .shortDescription(event.getShortDescription())
                .sku(event.getSku())
                .slug(event.getSlug())
                .price(event.getPrice())
                .stockQuantity(event.getStockQuantity())
                .build();
    }
}