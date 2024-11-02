package com.ecommerce.paymentservice.paymentgateways;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayFactory {

    @Autowired
    private RazorPayPaymentGateway razorPayPaymentGateway;

    @Autowired
    private StripePaymentGateway stripePaymentGateway;

    public PaymentGateway getPaymentGateway() {
        // randomize selection of payment gateway
        return stripePaymentGateway;
    }
}
