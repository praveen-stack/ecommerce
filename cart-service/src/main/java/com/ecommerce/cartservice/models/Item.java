package com.ecommerce.cartservice.models;

import com.ecommerce.cartservice.dtos.Product;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;

@Data
public class Item implements Serializable {
    private Long productId;
    private Integer quantity;
    @Transient
    private Product product;
    @Transient
    private Double itemTotal;
}
