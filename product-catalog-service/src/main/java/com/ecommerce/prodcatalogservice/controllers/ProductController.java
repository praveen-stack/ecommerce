package com.ecommerce.prodcatalogservice.controllers;

import com.ecommerce.prodcatalogservice.dtos.CategoryDto;
import com.ecommerce.prodcatalogservice.dtos.CreateProductDto;
import com.ecommerce.prodcatalogservice.dtos.ProductDto;
import com.ecommerce.prodcatalogservice.models.Category;
import com.ecommerce.prodcatalogservice.models.Product;
import com.ecommerce.prodcatalogservice.services.ProductDocumentService;
import com.ecommerce.prodcatalogservice.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDocumentService productDocumentService;

    private ProductDto convertToDto(Product product){
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImage(product.getImage());
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(product.getCategory().getId());
        categoryDto.setName(product.getCategory().getName());
        dto.setCategory(categoryDto);
        return dto;
    }

    private Product convertToEntity(CreateProductDto productDto){
        Product product = new Product();
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setImage(productDto.getImage());
        Category category = new Category();
        category.setName(productDto.getCategory());
        product.setCategory(category);
        return product;
    }

    @GetMapping("/suggestions")
    public List<String> getProductSuggestions(@RequestParam(required = true) String titleSearchKey,
                                              @RequestParam(defaultValue = "10") Integer pageSize) {
        return productDocumentService.getProductSearchSuggestions(titleSearchKey, pageSize);
    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable Long id) {
        var product = productService.getProduct(id);
        return convertToDto(product);
    }
    @PostMapping
    public ProductDto createProduct(@RequestBody @Valid CreateProductDto productDto) {
        var product = convertToEntity(productDto);
        product = productService.createProduct(product);
        return convertToDto(product);
    }
    @PostMapping("/bulk")
    public List<ProductDto> bulkInsertProducts(@RequestBody List<CreateProductDto> productDtos) {
        List<Product> products = productDtos.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        List<Product> createdProducts = new ArrayList<>();
        for(Product product: products){
            createdProducts.add(productService.createProduct(product));
        }
        return createdProducts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
