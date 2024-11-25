package com.ecommerce.paymentservice.paymentgateways;

import com.ecommerce.paymentservice.dtos.PaymentLink;
import com.ecommerce.paymentservice.enums.Gateway;
import com.ecommerce.paymentservice.exceptions.PaymentGatewayException;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RazorPayPaymentGateway implements PaymentGateway {
    private static final Logger logger = LoggerFactory.getLogger(RazorPayPaymentGateway.class);
    @Autowired
    private RazorpayClient razorpayClient;

    @Override
    public PaymentLink createPaymentLink(Long orderId, Long userId, Double amount) throws PaymentGatewayException{
        try {
            JSONObject request = new JSONObject();
            request.put("amount", amount * 100);
            request.put("currency", "INR");
            request.put("reference_id", orderId);
            JSONObject notes = new JSONObject();
            notes.put("userId", userId);
            notes.put("orderId", orderId);
            request.put("notes", notes);
            var link = razorpayClient.paymentLink.create(request);
            PaymentLink paymentLink = new PaymentLink();
            paymentLink.setLink(link.get("short_url").toString());
            paymentLink.setLink(link.get("id").toString());
            return paymentLink;
        } catch (RazorpayException e) {
            logger.error("An error occurred creating payment link for order" + orderId + ": {}", e.getMessage(), e);
            throw new PaymentGatewayException(e.getMessage());
        }
    }

    @Override
    public Gateway getGateway() {
        return Gateway.RAZORPAY;
    }
}
