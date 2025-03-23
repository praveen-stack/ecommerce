package com.ecommerce.orderservice.services;

import com.ecommerce.orderservice.dtos.AuthorizedUser;
import com.ecommerce.orderservice.enums.PaymentMethod;
import com.ecommerce.orderservice.models.Order;
import java.util.List;

public interface OrderService {

    Order createOrder(AuthorizedUser user, Order order, Long billingAddressId, Long ShippingAddressId, PaymentMethod paymentMethod);

    Order getOrder(AuthorizedUser user, Long orderId);

    List<Order> getAllOrders(AuthorizedUser user);
}
