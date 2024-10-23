package com.ecommerce.prodcatalogservice.services;

import com.ecommerce.prodcatalogservice.models.Product;

public interface ProductService {

    Product createProduct(Product product);
    Product getProduct(Long id);
}
