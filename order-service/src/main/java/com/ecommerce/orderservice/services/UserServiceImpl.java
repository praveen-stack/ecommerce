package com.ecommerce.orderservice.services;

import com.ecommerce.orderservice.configuration.AppConfig;
import com.ecommerce.orderservice.dtos.AuthorizedUser;
import com.ecommerce.orderservice.exceptions.NotFoundException;
import com.ecommerce.orderservice.models.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private AppConfig appConfig;
    
    public Address getAddressById(Long addressId, AuthorizedUser user) {
        var endpoint = appConfig.getUserServiceEndpoint();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpoint + "/me/addresses/" + addressId);
        RequestEntity<Void> requestEntity = RequestEntity.get(builder.build().toUri())
                .header("Authorization", "Bearer " + user.getJwt())
                .build();
        var response = restTemplate.exchange(requestEntity, Address.class);
        if(response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new NotFoundException("Address not found");
        }
        return response.getBody();
    }
}
