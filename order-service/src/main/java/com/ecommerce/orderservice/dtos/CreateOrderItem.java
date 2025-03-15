package com.ecommerce.orderservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderItem {
    @NotNull
    private Long productId;
    @NotNull
    private Integer quantity;
    @NotNull
    private Double price;
}
