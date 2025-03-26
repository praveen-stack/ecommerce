package com.ecommerce.usermanagementservice.enums;

public final class KafkaTopics {

    private KafkaTopics() {
    }
    public static final String PAYMENT_UPDATE = "payment-update";
    public static final String CASHFREE_PAYMENTS = "cashfree-payments";
    public static final String PASSWORD_RESET_REQUEST = "password-reset-request";

}
