package com.ecommerce.paymentservice.exceptions;

public class PaymentGatewayException extends RuntimeException {
    public PaymentGatewayException(String message) {
        super(message);
    }
}
