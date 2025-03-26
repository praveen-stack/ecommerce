package com.ecommerce.orderservice.utils;

import com.ecommerce.orderservice.dtos.OrderDto;
import com.ecommerce.orderservice.dtos.OrderItemDto;
import com.ecommerce.orderservice.models.Order;
import java.util.ArrayList;

public class OrderHelper {
    
    public static OrderDto toDto(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setCreatedAt(order.getCreatedAt());
        orderDto.setUpdatedAt(order.getUpdatedAt());
        orderDto.setBillingAddress(order.getBillingAddress());
        orderDto.setShippingAddress(order.getShippingAddress());
        orderDto.setPaymentId(order.getPaymentId());
        orderDto.setItems(new ArrayList<>());
        orderDto.setStatus(order.getStatus());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setUserId(order.getUserId());
        order.getItems().forEach(item -> {
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setProductId(item.getProductId());
            orderItemDto.setQuantity(item.getQuantity());
            orderItemDto.setPrice(item.getPrice());
            orderItemDto.setTotalAmount(item.getTotalAmount());
            orderItemDto.setProductDescription(item.getProductDescription());
            orderItemDto.setProductImage(item.getProductImage());
            orderItemDto.setProductTitle(item.getProductTitle());
            orderDto.getItems().add(orderItemDto);
        });
        return orderDto;
    }
}
