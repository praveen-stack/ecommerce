package com.ecommerce.notificationservice.dtos;

import com.ecommerce.notificationservice.dtos.Address;
import com.ecommerce.notificationservice.dtos.OrderItemDto;
import com.ecommerce.notificationservice.enums.OrderStatus;

import lombok.Data;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
public class OrderDto implements Serializable {
    private Long id;
    private Long userId;
    private String userEmail;
    private OrderStatus status;
    private Double totalAmount;
    private Long paymentId;
    private Address shippingAddress;
    private Address billingAddress;
    private List<OrderItemDto> items;
    private Instant createdAt;
    private Instant updatedAt;
}
