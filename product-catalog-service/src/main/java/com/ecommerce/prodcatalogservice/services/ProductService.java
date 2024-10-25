package com.ecommerce.prodcatalogservice.services;

import com.ecommerce.prodcatalogservice.dtos.ProductSearchDto;
import com.ecommerce.prodcatalogservice.models.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

    Product createProduct(Product product);
    Product getProduct(Long id);
    Page<Product> getProducts(ProductSearchDto dto);

}
