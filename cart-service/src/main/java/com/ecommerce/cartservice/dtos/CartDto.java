package com.ecommerce.cartservice.dtos;

import lombok.Data;
import java.util.List;

@Data
public class CartDto {
    private String id;
    private Long userId;
    private List<ItemDto> items;
    double total;
}
