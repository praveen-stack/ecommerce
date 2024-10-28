package com.ecommerce.cartservice.services;

import com.ecommerce.cartservice.configuration.AppConfig;
import com.ecommerce.cartservice.dtos.PageResponse;
import com.ecommerce.cartservice.dtos.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCatalogServiceImpl implements ProductCatalogService {

    @Autowired
    private AppConfig appConfig;

    @Override
    public List<Product> getProductsByIds(List<Long> productIds, String jwtToken) {
        if(productIds.isEmpty()) {
            return List.of();
        }
        var endpoint = appConfig.getProductCatalogServiceEndpoint();
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpoint + "/products");
        productIds.forEach(id -> builder.queryParam("productIds", id));
        builder.queryParam("pageSize", productIds.size());
        ParameterizedTypeReference<PageResponse<Product>> responseType = new ParameterizedTypeReference<>() {};
        RequestEntity<Void> requestEntity = RequestEntity.get(builder.build().toUri())
                .header("Authorization", "Bearer " + jwtToken)
                .build();
        var response = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, requestEntity, responseType);
        if(response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Error listing products by ids");
        }
        return response.getBody().getContent().stream().collect(Collectors.toList());
    }
}
