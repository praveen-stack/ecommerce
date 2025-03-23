package com.ecommerce.cartservice.services;

import com.ecommerce.cartservice.dtos.AuthorizedUser;
import com.ecommerce.cartservice.dtos.PaymentDto;

public interface PaymentService {
    PaymentDto getPaymentById(AuthorizedUser user, Long paymentId);
} 