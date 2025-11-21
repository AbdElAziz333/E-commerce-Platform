package com.aziz.product_service.kafka;

import com.aziz.product_service.mapper.ProductMapper;
import com.aziz.product_service.model.ProductSearch;
import com.aziz.product_service.repository.search.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductIndexer {
    private final ProductSearchRepository searchRepository;
    private final ProductMapper mapper;

    @KafkaListener(topics = "product-events", groupId = "product-indexer-group")
    public void handleProductEvent(ProductEvent event) {
        switch (event.getType()) {
            case CREATE, UPDATE -> {
                ProductSearch product = mapper.eventToProductSearch(event);
                searchRepository.save(product);
                log.info("Indexed product {} into Elasticsearch", event.getProductId());
            }
            case DELETE -> {
                searchRepository.deleteById(event.getProductId());
                log.info("Deleted product {} from Elasticsearch", event.getProductId());
            }
            default -> log.warn("Unknown event type: {}", event.getType());
        }
    }
}