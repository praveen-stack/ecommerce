package com.ecommerce.paymentservice.dtos;

import com.ecommerce.paymentservice.enums.Gateway;
import com.ecommerce.paymentservice.enums.PaymentMethod;
import com.ecommerce.paymentservice.enums.PaymentStatus;
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
