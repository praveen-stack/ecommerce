package com.ecommerce.orderservice.services;

import com.ecommerce.orderservice.dtos.AuthorizedUser;
import com.ecommerce.orderservice.enums.PaymentMethod;
import com.ecommerce.orderservice.models.Order;

public interface OrderService {

    Order createOrder(AuthorizedUser user, Order order, Long billingAddressId, Long ShippingAddressId, PaymentMethod paymentMethod);
}