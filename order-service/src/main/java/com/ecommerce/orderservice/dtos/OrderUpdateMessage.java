package com.ecommerce.orderservice.dtos;

import lombok.Data;

@Data
public class OrderUpdateMessage {
    private OrderDto order;
    private Payment payment;    
}
