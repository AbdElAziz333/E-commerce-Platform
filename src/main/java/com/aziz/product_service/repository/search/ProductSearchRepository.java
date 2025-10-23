package com.aziz.product_service.repository.search;

import com.aziz.product_service.model.ProductSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductSearch, String> {

}