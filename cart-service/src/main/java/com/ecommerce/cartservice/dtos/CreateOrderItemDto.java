package com.ecommerce.cartservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderItemDto {
    @NotNull
    private Long productId;
    @NotNull
    private Integer quantity;
    @NotNull
    private Double price;
}