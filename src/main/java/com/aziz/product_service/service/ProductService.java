package com.aziz.product_service.service;

import com.aziz.product_service.config.KafkaConfig;
import com.aziz.product_service.dto.ProductDto;
import com.aziz.product_service.dto.ProductRegisterRequest;
import com.aziz.product_service.dto.ProductUpdateRequest;
import com.aziz.product_service.kafka.ProductEvent;
import com.aziz.product_service.mapper.ProductMapper;
import com.aziz.product_service.model.Product;
import com.aziz.product_service.repository.mongo.ProductMongoRepository;
import com.aziz.product_service.repository.search.ProductSearchRepository;
import com.aziz.product_service.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMongoRepository mongoRepository;
    private final ProductSearchRepository searchRepository;

    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

    private final ProductMapper mapper;
    private final KafkaConfig kafkaConfig;

    public List<ProductDto> getAllProducts() {
        return mongoRepository.findAll().stream().map(mapper::productToDto).toList();
    }

    public ProductDto getProductById(String id) {
        return mongoRepository.findById(id).map(mapper::productToDto).orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }

    public ProductDto addProduct(ProductRegisterRequest request) {
        Product product = mapper.registerRequestToProduct(request);
        mongoRepository.save(product);
        ProductEvent event = mapper.productToEvent(product);
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
        return mapper.productToDto(product);

    }

    public void deleteProductById(String id) {
        mongoRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

        mongoRepository.deleteById(id);
    }
}
