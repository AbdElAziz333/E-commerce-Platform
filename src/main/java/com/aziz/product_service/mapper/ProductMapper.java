package com.aziz.product_service.mapper;

import com.aziz.product_service.dto.ProductDto;
import com.aziz.product_service.dto.ProductRegisterRequest;
import com.aziz.product_service.kafka.ProductEvent;
import com.aziz.product_service.model.Product;
import com.aziz.product_service.model.ProductSearch;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
//@RequiredArgsConstructor
public class ProductMapper {
    public Product registerRequestToProduct(ProductRegisterRequest request) {
        return Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .shortDescription(request.getShortDescription())
                .sku(request.getSku())
                .slug(request.getSlug())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
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