package com.ecommerce.notificationservice.services;

import com.ecommerce.notificationservice.enums.OrderStatus;

public interface EmailService {
    void sendOrderConfirmationEmail(String to, String orderId, double totalAmount);
    void sendPaymentFailedEmail(String to, String orderId, OrderStatus status, String paymentLink);
    void sendOrderProcessingEmail(String to, String orderId, OrderStatus status, String paymentLink);
    void sendPasswordResetEmail(String to, String resetToken);
} 