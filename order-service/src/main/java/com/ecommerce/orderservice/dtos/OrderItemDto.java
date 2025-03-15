package com.ecommerce.orderservice.dtos;

import lombok.Data;
import java.io.Serializable;

@Data
public class OrderItemDto implements Serializable {
    private Long productId;
    private Integer quantity;
    private Double price;
    private Double totalAmount;
    private String productTitle;
    private String productImage;
    private String productDescription;
}
