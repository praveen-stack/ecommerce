package com.ecommerce.paymentservice.paymentgateways;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayFactory {


    @Autowired
    private CashFreeGateway cashFreeGateway;

    public PaymentGateway getPaymentGateway() {
        // randomize selection of payment gateway
        return cashFreeGateway;
    }
}
