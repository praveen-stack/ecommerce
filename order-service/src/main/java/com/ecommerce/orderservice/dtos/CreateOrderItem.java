package com.ecommerce.orderservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderItem {
    @NotNull
    private Long orderId;
    @NotNull
    private Long productId;
    @NotNull
    private Integer quantity;
    @NotNull
    private Double price;
    @NotNull
    private Double totalAmount;
    @NotNull
    private String productTitle;
    private String productImage;
    private String productDescription;

}
