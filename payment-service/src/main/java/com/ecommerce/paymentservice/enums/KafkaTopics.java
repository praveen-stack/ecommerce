package com.ecommerce.paymentservice.enums;

public final class KafkaTopics {

    private KafkaTopics() {
    }
    public static final String PAYMENT_UPDATE = "payment-update";
    public static final String CASHFREE_PAYMENTS = "cashfree-payments";
}
