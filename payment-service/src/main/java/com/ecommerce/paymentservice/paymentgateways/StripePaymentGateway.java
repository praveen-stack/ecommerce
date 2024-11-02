package com.ecommerce.paymentservice.paymentgateways;

import com.ecommerce.paymentservice.dtos.PaymentLink;
import com.ecommerce.paymentservice.enums.Gateway;
import com.ecommerce.paymentservice.exceptions.PaymentGatewayException;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StripePaymentGateway implements PaymentGateway {

    private static final Logger logger = LoggerFactory.getLogger(StripePaymentGateway.class);

//    @Autowired
//    private StripeClient stripeClient;

    @Override
    public PaymentLink createPaymentLink(Long orderId, Long userId, Double amount) {

        var sessionParams = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8005/payment/success")
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.PAYPAL)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.BACS_DEBIT)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("inr")
                                                .setUnitAmount((long)(amount * 100))
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Order:" + orderId)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();
        try {

            Session session = Session.create(sessionParams);
            PaymentLink paymentLink = new PaymentLink();
            paymentLink.setLink(session.getUrl());
            paymentLink.setId(session.getId());
            return paymentLink;
        } catch (StripeException e) {
            logger.error("An error occurred creating payment link for order" + orderId + ": {}", e.getMessage(), e);
            throw new PaymentGatewayException(e.getMessage());
        }
    }

    @Override
    public Gateway getGateway() {
        return Gateway.STRIPE;
    }
}
