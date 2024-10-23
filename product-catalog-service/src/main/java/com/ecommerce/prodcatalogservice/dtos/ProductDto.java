package com.ecommerce.prodcatalogservice.dtos;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String title;
    private Double price;
    private String description;
    private CategoryDto category;
    private String image;
}
