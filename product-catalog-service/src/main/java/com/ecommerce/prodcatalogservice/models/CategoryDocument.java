package com.ecommerce.prodcatalogservice.models;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class CategoryDocument {
    @Field(type = FieldType.Long)
    private Long id;
    @Field(type = FieldType.Text)
    private String name;
}
