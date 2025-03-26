package com.ecommerce.orderservice.controllers;

import com.ecommerce.orderservice.dtos.AuthorizedUser;
import com.ecommerce.orderservice.dtos.CreateOrderDto;
import com.ecommerce.orderservice.dtos.OrderDto;
import com.ecommerce.orderservice.models.Order;
import com.ecommerce.orderservice.models.OrderItem;
import com.ecommerce.orderservice.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import com.ecommerce.orderservice.utils.OrderHelper;
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping
    public OrderDto createOrder(Authentication authentication, @RequestBody CreateOrderDto createOrderDto) {
        AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
        Order order = new Order();
        var orderItems = createOrderDto.getItems().stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            return orderItem;
        }).collect(Collectors.toList());
        order.setItems(orderItems);
        order = orderService.createOrder(
                authUser,order,
                createOrderDto.getBillingAddressId(),
                createOrderDto.getShippingAddressId(),
                createOrderDto.getPaymentMethod()
        );
        return OrderHelper.toDto(order);
    }
    @GetMapping("/{id}")
    public OrderDto getOrder(Authentication authentication, @PathVariable Long id) {
        AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
        Order order = orderService.getOrder(authUser,id);
        return OrderHelper.toDto(order);
    }

    @GetMapping
    public List<OrderDto> getAllOrders(Authentication authentication) {
        AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
        List<Order> orders = orderService.getAllOrders(authUser);
        return orders.stream()
                .map(OrderHelper::toDto)
                .collect(Collectors.toList());
    }
}
