package com.ecommerce.prodcatalogservice.services;

import com.ecommerce.prodcatalogservice.models.ProductDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchPage;

import java.util.List;

public interface ProductDocumentService {
    void indexProduct(ProductDocument product);
    List<String> getProductSearchSuggestions(String titleSearchKey, Integer pageSize);

    SearchPage<ProductDocument> searchProducts(Long categoryId, String keyword, Pageable pageable);
}
