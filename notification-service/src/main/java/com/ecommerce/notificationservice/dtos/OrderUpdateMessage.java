package com.ecommerce.notificationservice.dtos;

import lombok.Data;

@Data
public class OrderUpdateMessage {
    private OrderDto order;
    private PaymentDto payment;    
}
