package com.ecommerce.cartservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutResponseDto {
    private OrderDto order;
    private PaymentDto payment;
}
