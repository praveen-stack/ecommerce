package com.ecommerce.paymentservice.paymentgateways;

import com.ecommerce.paymentservice.dtos.AuthorizedUser;
import com.ecommerce.paymentservice.dtos.PaymentLink;
import com.ecommerce.paymentservice.enums.Gateway;
import com.ecommerce.paymentservice.enums.PaymentMethod;
import com.ecommerce.paymentservice.exceptions.PaymentGatewayException;

public interface PaymentGateway {
    PaymentLink createPaymentLink(Long orderId, AuthorizedUser user, Double amount, PaymentMethod paymentMethod) throws PaymentGatewayException;
    Gateway getGateway();
}
