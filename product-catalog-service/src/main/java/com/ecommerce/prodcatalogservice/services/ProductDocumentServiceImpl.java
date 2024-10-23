package com.ecommerce.prodcatalogservice.services;

import com.ecommerce.prodcatalogservice.models.ProductDocument;
import com.ecommerce.prodcatalogservice.repositories.ProductDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductDocumentServiceImpl implements ProductDocumentService {

    @Autowired
    private ProductDocumentRepository productDocumentRepository;

    @Override
    public void indexProduct(ProductDocument product) {
        this.productDocumentRepository.save(product);
    }
}
