package com.ecommerce.paymentservice.configuration;

import com.cashfree.Cashfree;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CashfreeConfig {

    @Value("${cashfree.clientId}")
    private String clientId;
    @Value("${cashfree.secret}")
    private String clientSecret;
    @Bean
    public Cashfree cashfree() {
        Cashfree.XClientId = clientId;
        Cashfree.XClientSecret = clientSecret;
        Cashfree.XEnvironment = Cashfree.SANDBOX;
        return new Cashfree();
    }
}
