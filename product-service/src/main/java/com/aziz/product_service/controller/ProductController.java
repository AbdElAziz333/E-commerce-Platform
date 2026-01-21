package com.aziz.product_service.controller;

import com.aziz.product_service.request.CreateProductRequest;
import com.aziz.product_service.dto.ProductDto;
import com.aziz.product_service.request.UpdateProductRequest;
import com.aziz.product_service.service.ProductService;
import com.aziz.product_service.util.ApiResponse;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductDto>>> getProducts(
            @RequestParam(defaultValue = "0") @Min(0) int page
    ) {
        return ResponseEntity.ok(ApiResponse.success("Products fetched successfully", service.getProducts(page)));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductBySlug(
            @PathVariable String slug
    ) {
        return ResponseEntity.ok(ApiResponse.success("Product fetched successfully", service.getProductBySlug(slug)));
    }

    @GetMapping("/u")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProductsByUserId(
            @RequestHeader("User-Id") Long userId
    ) {
        return ResponseEntity.ok(ApiResponse.success("Products fetched successfully", service.getProductsByUserId(userId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(
            @RequestHeader("User-Id") Long userId,
            @RequestBody CreateProductRequest registerRequest
    ) {
        return ResponseEntity.ok(ApiResponse.success("Product Created Successfully", service.createProduct(userId, registerRequest)));
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @RequestHeader("User-Id") Long userId,
            @PathVariable String productId,
            @RequestBody UpdateProductRequest updateRequest
    ) {
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", service.updateProduct(userId, productId, updateRequest)));
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(
            @RequestHeader("User-Id") Long userId,
            @PathVariable String productId
    ) {
        service.deleteProductById(userId, productId);
    }
}