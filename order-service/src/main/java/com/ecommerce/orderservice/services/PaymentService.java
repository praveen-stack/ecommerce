package com.ecommerce.orderservice.services;

import com.ecommerce.orderservice.dtos.AuthorizedUser;
import com.ecommerce.orderservice.dtos.CreatePaymentDto;
import com.ecommerce.orderservice.dtos.Payment;

public interface PaymentService {
    Payment createPayment(AuthorizedUser user, CreatePaymentDto createPaymentDto);
}
