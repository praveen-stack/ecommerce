package com.ecommerce.paymentservice.configuration;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorPayConfig {

    @Value("${razorpay.keyId}")
    private String keyId;

    @Value("${razorpay.keySecret}")
    private String keySecret;
    @Bean
    public RazorpayClient razorpayClient() throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);
        return razorpayClient;
    }
}
