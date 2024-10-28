package com.ecommerce.cartservice.services;

import com.ecommerce.cartservice.dtos.Product;

import java.util.List;

public interface ProductCatalogService {
    List<Product> getProductsByIds(List<Long> productIds, String jwtToken);
}
