package com.ecommerce.orderservice.services;

import com.ecommerce.orderservice.configuration.AppConfig;
import com.ecommerce.orderservice.dtos.AuthorizedUser;
import com.ecommerce.orderservice.dtos.CreatePaymentDto;
import com.ecommerce.orderservice.dtos.Payment;
import com.ecommerce.orderservice.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private AppConfig appConfig;

    @Override
    public Payment createPayment(AuthorizedUser user, CreatePaymentDto createPaymentDto) {

        var endpoint = appConfig.getPaymentServiceEndpoint();
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<CreatePaymentDto> requestEntity = RequestEntity.post(endpoint + "/payments")
                .header("Authorization", "Bearer " + user.getJwt())
                .body(createPaymentDto);
        var response = restTemplate.exchange(requestEntity, Payment.class);
        if(response.getStatusCode()== HttpStatusCode.valueOf(400)){
            throw new BadRequestException("Invalid payment request");
        }
        return response.getBody();
    }
}
