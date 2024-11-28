package com.ecommerce.orderservice.dtos;

import com.ecommerce.orderservice.enums.PaymentMethod;
import com.ecommerce.orderservice.enums.PaymentStatus;
import lombok.Data;
import java.time.Instant;

@Data
public class Payment {
    private Long id;
    private Long userId;
    private Long orderId;
    private String gateway;
    private String gatewayPaymentId;
    private PaymentMethod paymentMethod;
    private String paymentLink;
    private PaymentStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
