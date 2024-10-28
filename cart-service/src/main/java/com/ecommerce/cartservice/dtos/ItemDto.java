package com.ecommerce.cartservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemDto {
    private Long productId;
    private Integer quantity;
    private Product product;
    private Double itemTotal;
}
