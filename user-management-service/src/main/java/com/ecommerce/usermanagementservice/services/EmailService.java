package com.ecommerce.usermanagementservice.services;

public interface EmailService {
    void sendPasswordResetEmail(String to, String resetToken);
} 