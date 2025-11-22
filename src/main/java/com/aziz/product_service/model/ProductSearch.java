package com.aziz.product_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "products")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSearch {
    @Id
    private String productId;

    @MultiField(mainField = @Field(type = FieldType.Text),
        otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String name;

    @MultiField(mainField = @Field(type = FieldType.Text),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String description;

    @MultiField(mainField = @Field(type = FieldType.Text),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String shortDescription;

    @Field(type = FieldType.Keyword)
    private String sku;

    @MultiField(mainField = @Field(type = FieldType.Text),
            otherFields = {@InnerField(suffix = "keyword", type = FieldType.Keyword)})
    private String slug;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Integer)
    private Integer stockQuantity;
}