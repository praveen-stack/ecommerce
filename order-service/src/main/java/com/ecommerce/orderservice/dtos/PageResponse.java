package com.ecommerce.orderservice.dtos;

import lombok.Data;
import java.util.List;
@Data
public class PageResponse<T> {
    private List<T> content;
    private Boolean last;
    private Boolean first;
    private Long totalPages;
    private Long size;
    private Boolean empty;
}


