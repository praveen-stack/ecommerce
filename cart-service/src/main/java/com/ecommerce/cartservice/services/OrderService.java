package com.ecommerce.cartservice.services;

import com.ecommerce.cartservice.dtos.AuthorizedUser;
import com.ecommerce.cartservice.dtos.CreateOrderDto;
import com.ecommerce.cartservice.dtos.OrderDto;

public interface OrderService {
    OrderDto createOrder(AuthorizedUser user, CreateOrderDto createOrderRequest);
}
