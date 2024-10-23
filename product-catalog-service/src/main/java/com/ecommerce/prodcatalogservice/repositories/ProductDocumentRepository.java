package com.ecommerce.prodcatalogservice.repositories;

import com.ecommerce.prodcatalogservice.models.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDocumentRepository extends ElasticsearchRepository<ProductDocument, Long> {
}
