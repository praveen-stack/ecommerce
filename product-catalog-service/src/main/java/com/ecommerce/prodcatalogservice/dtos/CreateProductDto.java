package com.ecommerce.prodcatalogservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductDto {
    @NotBlank
    private String title;
    @NotNull
    private Double price;
    @NotBlank
    private String description;
    @NotBlank
    private String category;
    @NotBlank
    private String image;
}
