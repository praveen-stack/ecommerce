package com.ecommerce.paymentservice.paymentgateways;

import com.ecommerce.paymentservice.dtos.PaymentLink;
import com.ecommerce.paymentservice.enums.Gateway;

public interface PaymentGateway {
    PaymentLink createPaymentLink(Long orderId, Long userId, Double amount);
    Gateway getGateway();
}
