package com.aziz.product_service.service;

import com.aziz.product_service.request.CreateProductRequest;
import com.aziz.product_service.dto.ProductDto;
import com.aziz.product_service.request.UpdateProductRequest;
import com.aziz.product_service.mapper.ProductMapper;
import com.aziz.product_service.model.Product;
import com.aziz.product_service.repository.ProductRepository;
import com.aziz.product_service.util.exceptions.NotFoundException;
import com.aziz.product_service.util.exceptions.ProductAccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Transactional(readOnly = true)
    public Page<ProductDto> getProducts(int page) {
        Pageable pageable = PageRequest.of(page, 100, Sort.by("createdAt").ascending());
        return repository.findAll(pageable).map(mapper::productToDto);
    }

    @Transactional(readOnly = true)
    public ProductDto getProductBySlug(String slug) {
        log.debug("Fetching product: {}", slug);
        return repository.findBySlug(slug)
                .map(mapper::productToDto)
                .orElseThrow(() -> new NotFoundException("Product not found with slug: " + slug));
    }

    @Transactional
    public ProductDto createProduct(Long userId, CreateProductRequest request) {
        log.debug("Creating product for user: {}", userId);

        Product product = mapper.creationRequestToProduct(request);
        product.setUserId(userId);

        Product savedProduct = repository.save(product);
        log.info("Product {} created successfully for user {}", savedProduct.getId(), userId);

        return mapper.productToDto(product);
    }

    @Transactional
    public ProductDto updateProduct(Long userId, String productId, UpdateProductRequest request) {
        Product product = getProductForUser(userId, productId);

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setShortDescription(request.getShortDescription());
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());

//        repository.save(product);
        log.info("Product: {} updated for user: {}", productId, userId);

        return mapper.productToDto(product);
    }

    @Transactional
    public void deleteProductById(Long userId, String productId) {
        repository.delete(getProductForUser(userId, productId));
        log.info("Product: {} deleted for user {}", productId, userId);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByUserId(Long userId) {
        log.debug("Attempting to fetch products with user id: {}", userId);
        List<ProductDto> products = repository.findProductsByUserId(userId).stream().map(mapper::productToDto).toList();
        log.info("Successfully fetched {} product for user with id: {}", products.size(), userId);
        return products;
    }

    private Product getProductForUser(Long userId, String productId) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found" + productId));

        if (!product.getUserId().equals(userId)) {
            throw new ProductAccessDeniedException("Access denied for product: " + productId);
        }

        return product;
    }
}