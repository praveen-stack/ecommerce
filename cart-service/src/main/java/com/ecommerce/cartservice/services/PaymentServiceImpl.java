package com.ecommerce.cartservice.services;

import com.ecommerce.cartservice.configuration.AppConfig;
import com.ecommerce.cartservice.dtos.AuthorizedUser;
import com.ecommerce.cartservice.dtos.PaymentDto;
import com.ecommerce.cartservice.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private AppConfig appConfig;

    @Override
    public PaymentDto getPaymentById(AuthorizedUser user, Long paymentId) {
        var endpoint = appConfig.getPaymentServiceEndpoint();
        RestTemplate restTemplate = new RestTemplate();
        
        RequestEntity<Void> requestEntity = RequestEntity
            .get(endpoint + "/payments/" + paymentId)
            .header("Authorization", "Bearer " + user.getJwt())
            .build();

        ResponseEntity<PaymentDto> paymentResponse = restTemplate.exchange(requestEntity, PaymentDto.class);
        
        if (paymentResponse.getStatusCode() != HttpStatus.OK) {
            throw new BadRequestException("Failed to fetch payment details");
        }
        
        return paymentResponse.getBody();
    }
} 