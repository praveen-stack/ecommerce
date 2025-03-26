package com.ecommerce.notificationservice.dtos;

import com.ecommerce.notificationservice.enums.Gateway;
import com.ecommerce.notificationservice.enums.PaymentMethod;
import com.ecommerce.notificationservice.enums.PaymentStatus;
import lombok.Data;
import java.time.Instant;

@Data
public class PaymentDto {
    private Long id;
    private Long userId;
    private Long orderId;
    private Gateway gateway;
    private String gatewayPaymentId;
    private PaymentMethod paymentMethod;
    private String paymentLink;
    private PaymentStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
