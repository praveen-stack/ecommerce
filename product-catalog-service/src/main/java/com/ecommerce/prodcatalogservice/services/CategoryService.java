package com.ecommerce.prodcatalogservice.services;

import com.ecommerce.prodcatalogservice.models.Category;

public interface CategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
}
