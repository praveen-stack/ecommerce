package com.ecommerce.cartservice.configuration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.MacAlgorithm;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@PropertySource(value = "classpath:config.properties", ignoreResourceNotFound = true)
public class AppConfig {
    @Bean
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }

    @Autowired
    private Environment environment;

    public String getProductCatalogServiceEndpoint() {
        return environment.getProperty("product.catalog.service.endpoint");
    }

    public String getOrderServiceEndpoint() {
        return environment.getProperty("order.service.endpoint");
    }

    public String getPaymentServiceEndpoint() {
        return environment.getProperty("payment.service.endpoint");
    }

    @Bean
    public SecretKey getSecretKey(){
        MacAlgorithm algorithm = Jwts.SIG.HS256;
        String secret = environment.getProperty("jwt.secret");
        if(secret==null){
            // fallback to default
            return algorithm.key().build();
        }
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        return secretKey;
    }

}
