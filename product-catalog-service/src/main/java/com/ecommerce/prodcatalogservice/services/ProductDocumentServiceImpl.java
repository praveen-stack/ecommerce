package com.ecommerce.prodcatalogservice.services;

import com.ecommerce.prodcatalogservice.models.ProductDocument;
import com.ecommerce.prodcatalogservice.repositories.ProductDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDocumentServiceImpl implements ProductDocumentService {

    @Autowired
    private ProductDocumentRepository productDocumentRepository;

    @Override
    public void indexProduct(ProductDocument product) {
        this.productDocumentRepository.save(product);
    }

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public List<String> getProductSearchSuggestions(String titleSearchKey, Integer pageSize) {
        Query searchQuery = NativeQuery.builder()
                .withQuery(q ->
                        q.fuzzy(
                                f ->f.field("title")
                                        .value(titleSearchKey)
                                        .fuzziness("AUTO")
                        )
                )
                .withPageable(Pageable.ofSize(pageSize))  // Return top 10 results
                .build();
        SearchHits<ProductDocument> hits = elasticsearchOperations.search(searchQuery, ProductDocument.class);
        return hits.getSearchHits().stream().map(hit -> hit.getContent().getTitle()).toList();
    }
}
