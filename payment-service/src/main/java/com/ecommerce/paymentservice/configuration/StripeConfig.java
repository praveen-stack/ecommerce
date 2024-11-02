package com.ecommerce.paymentservice.configuration;

import com.stripe.Stripe;
import com.stripe.StripeClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${stripe.apiKey}")
    private String apiKey;
    @Bean
    public StripeClient stripe() {
        Stripe.apiKey = apiKey;
        var builder = new StripeClient.StripeClientBuilder();
        return builder.setApiKey(apiKey).build();
    }
}
