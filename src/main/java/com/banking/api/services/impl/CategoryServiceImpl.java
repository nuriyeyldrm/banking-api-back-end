package com.banking.api.services.impl;

import com.banking.api.domain.Category;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;
import com.banking.api.repositories.CategoryRepository;
import com.banking.api.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> fetchAllCategories(Integer userId) {
        return categoryRepository.findAll(userId);
    }

    @Override
    public Category fetchCategoryById(Integer userId, Integer categoryId) throws BankResourceNotFoundException {
        return categoryRepository.findById(userId, categoryId);
    }

    @Override
    public Category addCategory(Integer userId, String title, String description) throws BankBadRequestException {
        int categoryId = categoryRepository.create(userId, title, description);
        return categoryRepository.findById(userId, categoryId);
    }

    @Override
    public void updateCategory(Integer userId, Integer categoryId, Category category) throws BankBadRequestException {
        categoryRepository.update(userId, categoryId, category);
    }

    @Override
    public void removeCategoryWithAllTransactions(Integer userId, Integer categoryId)
            throws BankResourceNotFoundException {
        this.fetchCategoryById(userId, categoryId);
        categoryRepository.removeById(userId, categoryId);
    }
}
