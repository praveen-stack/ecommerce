package com.ecommerce.paymentservice.services;

import com.ecommerce.paymentservice.dtos.AuthorizedUser;
import com.ecommerce.paymentservice.enums.PaymentMethod;
import com.ecommerce.paymentservice.enums.PaymentStatus;
import com.ecommerce.paymentservice.models.Payment;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaymentService {
    Payment createPayment(AuthorizedUser user, Long orderId, Double amount, PaymentMethod paymentMethod);

    Payment getPaymentById(AuthorizedUser user, Long paymentId);

    Payment updatePaymentStatus(String gatewayPaymentId, PaymentStatus status) throws JsonProcessingException;
}
