package com.ecommerce.orderservice.dtos;

import com.ecommerce.orderservice.enums.OrderStatus;
import com.ecommerce.orderservice.models.Address;
import lombok.Data;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
public class OrderDto implements Serializable {
    private Long id;
    private Long userId;
    private OrderStatus status;
    private Double totalAmount;
    private Long paymentId;
    private Address shippingAddress;
    private Address billingAddress;
    private List<OrderItemDto> items;
    private Instant createdAt;
    private Instant updatedAt;
}
