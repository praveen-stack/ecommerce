package com.ecommerce.cartservice.dtos;

import lombok.Data;

@Data
public class ItemDto {
    private Long productId;
    private Integer quantity;
    private Product product;
    private Double itemTotal;
}
