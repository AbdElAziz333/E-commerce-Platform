package com.aziz.product_service.controller;

import com.aziz.product_service.dto.ProductDto;
import com.aziz.product_service.dto.ProductRegisterRequest;
import com.aziz.product_service.dto.ProductUpdateRequest;
import com.aziz.product_service.model.ProductSearch;
import com.aziz.product_service.repository.search.ProductSearchRepository;
import com.aziz.product_service.service.ProductService;
import com.aziz.product_service.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    private final ProductSearchRepository searchRepo;
    private final ElasticsearchOperations esOps;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched all products", service.getAllProducts()));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable String productId) {
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched product by id: " + productId, service.getProductById(productId)));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("q") String q,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {

        // A minimal string query (you can build a full DSL JSON query)
        String json = "{\n" +
                "  \"query\": {\n" +
                "    \"multi_match\": {\n" +
                "      \"query\": \"" + q + "\",\n" +
                "      \"fields\": [\"name^3\", \"description\", \"shortDescription\"]\n" +
                "    }\n" +
                "  }\n" +
                "}";

        Query query = new StringQuery(json);
        query.setPageable(PageRequest.of(page, size));

        SearchHits<ProductSearch> hits = esOps.search(query, ProductSearch.class);
        return ResponseEntity.ok(hits.get().map(SearchHit::getContent).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> addProduct(@RequestBody ProductRegisterRequest registerRequest) {
        return ResponseEntity.ok(ApiResponse.success("Successfully registered product", service.addProduct(registerRequest)));
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
}