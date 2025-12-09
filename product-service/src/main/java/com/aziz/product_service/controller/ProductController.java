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

    //TODO: fix  this error
    //org.elasticsearch.client.ResponseException: method [POST], host [http://localhost:9200], URI [/products/_search?typed_keys=true], status line [HTTP/1.1 503 Service Unavailable]
    //{"error":{"root_cause":[{"type":"no_shard_available_action_exception","reason":null}],"type":"search_phase_execution_exception","reason":"all shards failed","phase":"query","grouped":true,"failed_shards":[{"shard":0,"index":"products","node":null,"reason":{"type":"no_shard_available_action_exception","reason":null}}]},"status":503}
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductSearch>>> search(
            @RequestParam("q") String q,
            @RequestParam(defaultValue = "0") int page
    ) throws IOException {

        int pageSize = 75;

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
                        .from(page * pageSize)
                        .size(pageSize),
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
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProductsByUserId(@RequestHeader("User-Id") Long userId) {
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched all products for user with id: " + userId, service.getProductsByUserId(userId)));
    }
}