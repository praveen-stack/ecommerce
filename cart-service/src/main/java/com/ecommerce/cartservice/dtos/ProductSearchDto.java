package com.ecommerce.cartservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ProductSearchDto {
    public ProductSearchDto () {
        this.pageSize = 10;
        this.pageNumber = 0;
        this.sortBy = "price";
        this.sortOrder = "asc";
    }

    private Long categoryId;
    private List<Long> productIds;
    private String query;
    private Integer pageSize;
    private Integer pageNumber;
    private String sortBy;
    private String sortOrder;
}
