package com.ecommerce.orderservice.services;

import com.ecommerce.orderservice.enums.OrderStatus;

public interface EmailService {
    void sendOrderConfirmationEmail(String to, String orderId, double totalAmount);
    void sendPaymentFailedEmail(String to, String orderId, OrderStatus status, String paymentLink);
    void sendOrderProcessingEmail(String to, String orderId, OrderStatus status, String paymentLink);
} 