package com.ecommerce.orderservice.services;

import com.ecommerce.orderservice.dtos.Product;

import java.util.List;

public interface ProductCatalogService {
    List<Product> getProductsByIds(List<Long> productIds, String jwtToken);
}
