package com.ecommerce.orderservice.enums;

public final class KafkaTopics {

    private KafkaTopics() {
    }
    public static final String PAYMENT_UPDATE = "payment-update";
    public static final String CASHFREE_PAYMENTS = "cashfree-payments";
    public static final String PASSWORD_RESET_REQUEST = "password-reset-request";
    public static final String ORDER_PROCESSING = "order-processing";
    public static final String ORDER_CONFIRMATION = "order-confirmation";
    public static final String ORDER_PAYMENT_FAILED = "order-payment-failed";
}
