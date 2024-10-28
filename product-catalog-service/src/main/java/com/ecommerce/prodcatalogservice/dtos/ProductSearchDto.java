package com.ecommerce.prodcatalogservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class ProductSearchDto {
    private Long categoryId;
    private String query;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Min(value = 1, message = "pageSize must be at least 1")
    private Integer pageSize = 10;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Min(value = 0, message = "pageNumber must be at least 0")
    private Integer pageNumber = 0;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Pattern(regexp = "title|id|price", message = "sortBy must be one of the following: title, id, price")
    private String sortBy = "price";
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Pattern(regexp = "asc|desc", message = "sortOrder must be one of the following: asc, desc")
    private String sortOrder = "asc";
    private List<Long> productIds;
}
