package com.ecommerce.cartservice.dtos;

import com.ecommerce.cartservice.enums.Gateway;
import com.ecommerce.cartservice.enums.PaymentMethod;
import com.ecommerce.cartservice.enums.PaymentStatus;
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
