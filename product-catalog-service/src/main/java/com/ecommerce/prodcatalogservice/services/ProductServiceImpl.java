package com.ecommerce.prodcatalogservice.services;

import com.ecommerce.prodcatalogservice.exceptions.NotFoundException;
import com.ecommerce.prodcatalogservice.models.CategoryDocument;
import com.ecommerce.prodcatalogservice.models.Product;
import com.ecommerce.prodcatalogservice.models.ProductDocument;
import com.ecommerce.prodcatalogservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDocumentService productDocumentService;
    @Override
    @Transactional
    public Product createProduct(Product product) {
        if (product.getCategory() != null) {
            product.setCategory(this.categoryService.getCategoryByName(product.getCategory().getName()));
        }
        product = this.productRepository.save(product);
        indexProduct(product);
        return product;
    }

    @Override
    public Product getProduct(Long id) {
        var product = this.productRepository.findById(id);
        if (!product.isPresent()) {
            throw new NotFoundException("Product not found");
        }
        return product.get();
    }

    private void indexProduct(Product product) {
        ProductDocument document = new ProductDocument();
        document.setId(product.getId());
        document.setTitle(product.getTitle());
        document.setDescription(product.getDescription());
        document.setPrice(product.getPrice());
        CategoryDocument categoryDocument = new CategoryDocument();
        categoryDocument.setId(product.getCategory().getId());
        categoryDocument.setName(product.getCategory().getName());
        document.setCategory(categoryDocument);
        productDocumentService.indexProduct(document);
    }
}
