package com.ecommerce.prodcatalogservice.services;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import com.ecommerce.prodcatalogservice.models.ProductDocument;
import com.ecommerce.prodcatalogservice.repositories.ProductDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

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

    public SearchPage<ProductDocument> searchProducts(Long categoryId, List<Long> productIds, String keyword, Pageable pageable) {
        var builder = NativeQuery.builder();
        if(categoryId != null || keyword != null || (productIds != null && productIds.size() > 0)){
            builder.withQuery(q ->
                    q.bool(
                            bq -> {
                                if(categoryId != null){
                                    bq.must(b -> b.match(m -> m.field("category.id").query(categoryId)));
                                }
                                if(keyword != null){
                                    bq.must(b -> b.multiMatch(m -> m.fields("title", "description").query(keyword)));
                                }
                                if(productIds != null && productIds.size() > 0){
                                    List<FieldValue> fieldValues = productIds.stream()
                                            .map(FieldValue::of)
                                            .collect(Collectors.toList());

                                    TermsQuery tq = TermsQuery.of(b-> b.field("id").terms(t -> t.value(fieldValues)));
                                    bq.must(b-> b.terms(tq));
                                }
                                return bq;
                            }
                    )
            );
        }
        builder.withPageable(pageable);
        Query searchQuery = builder.build();
        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(searchQuery, ProductDocument.class);
        return SearchHitSupport.searchPageFor(searchHits, pageable);
    }
}
