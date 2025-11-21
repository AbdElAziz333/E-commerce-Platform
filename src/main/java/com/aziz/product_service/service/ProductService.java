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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMongoRepository mongoRepository;

    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

    private final ProductMapper mapper;
    private final KafkaConfig kafkaConfig;

    public List<ProductDto> getAllProducts() {
        return mongoRepository.findAll().stream().map(mapper::productToDto).toList();
    }

    public ProductDto getProductBySlug(String slug) {
        return mongoRepository.findBySlug(slug).map(mapper::productToDto).orElseThrow(() -> new NotFoundException("Product not found with slug: " + slug));
    }

    public ProductDto createProduct(Long userId, ProductCreationRequest request) {
        Product product = mapper.creationRequestToProduct(request);
        product.setUserId(userId);

        mongoRepository.save(product);
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

    public ProductDto updateProduct(ProductUpdateRequest request) {
        Product product = mongoRepository.findById(request.getProductId()).orElseThrow(() -> new NotFoundException("Product not found with id: " + request.getProductId()));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setShortDescription(request.getShortDescription());
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());

        mongoRepository.save(product);

        ProductEvent event = mapper.productToEvent(product);
        event.setType(ProductEventType.UPDATE);
        kafkaTemplate.send(kafkaConfig.getTopic(), product.getProductId(), event);

        return mapper.productToDto(product);
    }

    public void deleteProductById(String id) {
        mongoRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

        mongoRepository.deleteById(id);
        ProductEvent event = ProductEvent.builder()
                .type(ProductEventType.DELETE)
                .productId(id)
                .build();

        kafkaTemplate.send(kafkaConfig.getTopic(), id, event);
    }

    public List<ProductDto> getProductsByUserId(Long userId) {
        List<ProductDto> productDtos = mongoRepository.findProductsByUserId(userId).stream().map(mapper::productToDto).toList();

        return productDtos;
    }
}
