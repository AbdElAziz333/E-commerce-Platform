package com.aziz.product_service.kafka;

import com.aziz.product_service.mapper.ProductMapper;
import com.aziz.product_service.model.ProductSearch;
import com.aziz.product_service.repository.search.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductIndexer {
    private final ProductSearchRepository searchRepository;
    private final ProductMapper mapper;

    @KafkaListener(topics = "product-events", groupId = "product-indexer-group")
    public void handleProductEvent(ProductEvent event) {
        ProductSearch product = mapper.eventToProductSearch(event);
        searchRepository.save(product);
    }
}