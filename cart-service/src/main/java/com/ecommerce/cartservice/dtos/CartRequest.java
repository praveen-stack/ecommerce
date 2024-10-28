package com.ecommerce.cartservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartRequest {

    @NotNull
    private Long productId;
    @NotNull
    private Integer quantity;
}
