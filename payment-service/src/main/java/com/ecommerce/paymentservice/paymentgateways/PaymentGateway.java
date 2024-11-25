package com.ecommerce.paymentservice.paymentgateways;

import com.ecommerce.paymentservice.dtos.PaymentLink;
import com.ecommerce.paymentservice.enums.Gateway;
import com.ecommerce.paymentservice.exceptions.PaymentGatewayException;

public interface PaymentGateway {
    PaymentLink createPaymentLink(Long orderId, Long userId, Double amount) throws PaymentGatewayException;
    Gateway getGateway();
}
