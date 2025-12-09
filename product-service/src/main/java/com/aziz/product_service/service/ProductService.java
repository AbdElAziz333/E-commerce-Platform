package com.aziz.product_service.service;

import com.aziz.product_service.config.KafkaConfig;
import com.aziz.product_service.dto.ProductCreationRequest;
import com.aziz.product_service.dto.ProductDto;
import com.aziz.product_service.dto.ProductUpdateRequest;
import com.aziz.product_service.kafka.ProductEvent;
import com.aziz.product_service.mapper.ProductMapper;
import com.aziz.product_service.model.Product;
import com.aziz.product_service.repository.mongo.ProductMongoRepository;
import com.aziz.product_service.util.ProductEventType;
import com.aziz.product_service.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMongoRepository mongoRepository;

    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

    private final ProductMapper mapper;
    private final KafkaConfig kafkaConfig;

    //TODO: needs pagination
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        log.debug("Attempting to fetch all products");
        List<ProductDto> products = mongoRepository.findAll().stream().map(mapper::productToDto).toList();
        log.info("Fetched {} product", products.size());
        return products;
    }

    @Transactional(readOnly = true)
    public ProductDto getProductBySlug(String slug) {
        log.debug("Attempting to fetch product with slug: {}", slug);
        return mongoRepository.findBySlug(slug)
                .map(product -> {
                    log.info("Successfully fetched product with slug: {}", slug);
                    return mapper.productToDto(product);
                })
                .orElseThrow(() -> {
                    log.error("Cannot fetch product with slug: {}, product not found", slug);
                    return new NotFoundException("Product not found with slug: " + slug);
                });
    }

    @Transactional
    public ProductDto createProduct(Long userId, ProductCreationRequest request) {
        log.debug("Attempting to create a new product for user with id: {}", userId);
        Product product = mapper.creationRequestToProduct(request);
        product.setUserId(userId);

        Product savedProduct = mongoRepository.save(product);
        log.info("Product successfully created with id: {}, for user with id: {}", savedProduct.getProductId(), savedProduct.getUserId());

        ProductEvent event = ProductEvent.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .shortDescription(product.getShortDescription())
                .sku(product.getSku())
                .slug(product.getSlug())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .build();

        event.setType(ProductEventType.CREATE);

        kafkaTemplate.send(kafkaConfig.getTopic(), product.getProductId(), event);
        return mapper.productToDto(product);
    }

    //TODO: needs authentication so only who created the product can update it..
    @Transactional
    public ProductDto updateProduct(Long userId, ProductUpdateRequest request) {
        log.debug("Attempting to update a product with id: {}, for user with id: {}", request.getProductId(), userId);
        Product product = mongoRepository.findById(request.getProductId())
                .orElseThrow(() -> {
                    log.error("Cannot update product info -- product not found with id: {}", request.getProductId());
                    return new NotFoundException("Product not found with id: " + request.getProductId());
                });

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setShortDescription(request.getShortDescription());
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());

        mongoRepository.save(product);
        log.info("Product updated successfully with id: {}", request.getProductId());

        ProductEvent event = mapper.productToEvent(product);
        event.setType(ProductEventType.UPDATE);
        kafkaTemplate.send(kafkaConfig.getTopic(), product.getProductId(), event);

        return mapper.productToDto(product);
    }

    @Transactional
    public void deleteProductById(Long userId, String id) {
        log.debug("Attempting to delete product with id: {}, for user with id: {}", id, userId);

        mongoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot delete product with id: {}, product not found", id);
                    return new NotFoundException("Product not found with id: " + id);
                });

        mongoRepository.deleteById(id);
        log.info("Product deleted successfully with id: {}", id);

        ProductEvent event = ProductEvent.builder()
                .type(ProductEventType.DELETE)
                .productId(id)
                .build();

        kafkaTemplate.send(kafkaConfig.getTopic(), id, event);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByUserId(Long userId) {
        log.debug("Attempting to fetch products with user id: {}", userId);
        List<ProductDto> products = mongoRepository.findProductsByUserId(userId).stream().map(mapper::productToDto).toList();
        log.info("Successfully fetched {} product for user with id: {}", products.size(), userId);
        return products;
    }
}
