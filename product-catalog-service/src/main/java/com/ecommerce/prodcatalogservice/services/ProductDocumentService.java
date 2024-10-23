package com.ecommerce.prodcatalogservice.services;

import com.ecommerce.prodcatalogservice.models.ProductDocument;

import java.util.List;

public interface ProductDocumentService {
    void indexProduct(ProductDocument product);
    List<String> getProductSearchSuggestions(String titleSearchKey, Integer pageSize);
}
