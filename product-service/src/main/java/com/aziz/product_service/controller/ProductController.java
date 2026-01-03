package com.aziz.product_service.controller;

import com.aziz.product_service.dto.ProductCreationRequest;
import com.aziz.product_service.dto.ProductDto;
import com.aziz.product_service.dto.ProductUpdateRequest;
import com.aziz.product_service.service.ProductService;
import com.aziz.product_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched all products", service.getAllProducts()));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched product by slug: " + slug, service.getProductBySlug(slug)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(
            @RequestHeader("User-Id") Long userId,
            @RequestBody ProductCreationRequest registerRequest
    ) {
        return ResponseEntity.ok(ApiResponse.success("Successfully registered product", service.createProduct(userId, registerRequest)));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @RequestHeader("User-Id") Long userId,
            @RequestBody ProductUpdateRequest updateRequest
    ) {
        return ResponseEntity.ok(ApiResponse.success("Successfully updated product", service.updateProduct(userId, updateRequest)));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProductById(
            @RequestHeader("User-Id") Long userId,
            @PathVariable String productId
    ) {
        service.deleteProductById(userId, productId);
        return ResponseEntity.ok(ApiResponse.success("Successfully deleted product with id: " + productId, null));
    }

    @GetMapping("/u")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProductsByUserId(@RequestHeader("User-Id") Long userId) {
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched all products for user with id: " + userId, service.getProductsByUserId(userId)));
    }
}