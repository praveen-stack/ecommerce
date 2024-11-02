package com.ecommerce.paymentservice.services;

import com.ecommerce.paymentservice.dtos.AuthorizedUser;
import com.ecommerce.paymentservice.models.Payment;

public interface PaymentService {
    Payment createPayment(AuthorizedUser user, Long orderId, Double amount);

    Payment getPaymentById(AuthorizedUser user, Long paymentId);
}
