package com.aziz.product_service.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.aziz.product_service.dto.ProductCreationRequest;
import com.aziz.product_service.dto.ProductDto;
import com.aziz.product_service.dto.ProductUpdateRequest;
import com.aziz.product_service.model.ProductSearch;
import com.aziz.product_service.repository.search.ProductSearchRepository;
import com.aziz.product_service.service.ProductService;
import com.aziz.product_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    private final ProductSearchRepository searchRepo;
    private final ElasticsearchOperations esOps;
    private final ElasticsearchClient elasticsearchClient;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched all products", service.getAllProducts()));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched product by slug: " + slug, service.getProductBySlug(slug)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@RequestHeader("X-User-Id") Long userId, @RequestBody ProductCreationRequest registerRequest) {
        return ResponseEntity.ok(ApiResponse.success("Successfully registered product", service.createProduct(userId, registerRequest)));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(@RequestBody ProductUpdateRequest updateRequest) {
        return ResponseEntity.ok(ApiResponse.success("Successfully updated product", service.updateProduct(updateRequest)));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProductById(@PathVariable String productId) {
        service.deleteProductById(productId);
        return ResponseEntity.ok(ApiResponse.success("Successfully deleted product with id: " + productId, null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductSearch>>> search(@RequestParam("q") String q,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) throws IOException {

        // Build a multi_match query on analyzed text fields
        Query query = MultiMatchQuery.of(m -> m
                .query(q)
                .fields("name", "description", "shortDescription") // main text fields
                .fuzziness("AUTO") // allow minor typos
        )._toQuery();

        // Execute search
        SearchResponse<ProductSearch> response = elasticsearchClient.search(
                s -> s
                        .index("products")
                        .query(query)
                        .from(page * size)
                        .size(size),
                ProductSearch.class
        );

        // Map results
        List<ProductSearch> results = response.hits().hits()
                .stream()
                .map(hit -> hit.source())
                .filter(Objects::nonNull)
                .toList();

        System.out.println("Found " + results.size() + " products");

        return ResponseEntity.ok(ApiResponse.success("Search results", results));
    }

    @GetMapping("/u")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProductsByUserId(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched all products for user with id: " + userId, service.getProductsByUserId(userId)));
    }
}