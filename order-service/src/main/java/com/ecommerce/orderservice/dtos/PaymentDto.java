package com.ecommerce.orderservice.dtos;

import com.ecommerce.orderservice.enums.PaymentStatus;
import lombok.Data;
import java.time.Instant;

@Data
public class PaymentDto {
    private Long id;
    private Long userId;
    private Long orderId;
    private String gateway;
    private String gatewayPaymentId;
    private String paymentLink;
    private PaymentStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
