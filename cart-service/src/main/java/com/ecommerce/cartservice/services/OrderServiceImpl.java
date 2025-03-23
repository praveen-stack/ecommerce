package com.ecommerce.cartservice.services;

import com.ecommerce.cartservice.configuration.AppConfig;
import com.ecommerce.cartservice.dtos.AuthorizedUser;
import com.ecommerce.cartservice.dtos.CreateOrderDto;
import com.ecommerce.cartservice.dtos.OrderDto;
import com.ecommerce.cartservice.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private AppConfig appConfig;

    @Override
    public OrderDto createOrder(AuthorizedUser user, CreateOrderDto createOrderRequest) {
        var endpoint = appConfig.getOrderServiceEndpoint();
        RestTemplate restTemplate = new RestTemplate();
        
        RequestEntity<CreateOrderDto> requestEntity = RequestEntity
            .post(endpoint + "/orders")
            .header("Authorization", "Bearer " + user.getJwt())
            .body(createOrderRequest);

        ResponseEntity<OrderDto> orderResponse = restTemplate.exchange(requestEntity, OrderDto.class);
        
        if (orderResponse.getStatusCode() != HttpStatus.OK) {
            throw new BadRequestException("Failed to create order");
        }
        
        return orderResponse.getBody();
    }
} 