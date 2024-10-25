package com.ecommerce.prodcatalogservice.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProductSearchDto {
    public ProductSearchDto () {
        this.pageSize = 10;
        this.pageNumber = 0;
        this.sortBy = "title";
        this.sortOrder = "asc";
    }

    private Long categoryId;
    private String query;
    @Min(value = 1, message = "pageSize must be at least 1")
    private Integer pageSize;
    @Min(value = 0, message = "pageNumber must be at least 0")
    private Integer pageNumber;
    @Pattern(regexp = "title|id|price", message = "sortBy must be one of the following: title, id, price")
    private String sortBy;
    @Pattern(regexp = "asc|desc", message = "sortOrder must be one of the following: asc, desc")
    private String sortOrder;
}
