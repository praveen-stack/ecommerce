package com.ecommerce.prodcatalogservice.services;

import com.ecommerce.prodcatalogservice.models.Category;
import com.ecommerce.prodcatalogservice.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    @Transactional
    public Category getCategoryByName(String name) {
        var category = categoryRepository.findByName(name);
        if(category == null){
            category = new Category();
            category.setName(name);
            category = categoryRepository.save(category);
        }
        return category;
    }
}
