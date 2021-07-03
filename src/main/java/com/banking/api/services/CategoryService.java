package com.banking.api.services;

import com.banking.api.domain.Category;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;

import java.util.List;

public interface CategoryService {

    List<Category> fetchAllCategories(Integer userId);

    Category fetchCategoryById(Integer userId, Integer categoryId) throws BankResourceNotFoundException;

    Category addCategory(Integer userId, String title, String description) throws BankBadRequestException;

    void updateCategory(Integer userId, Integer categoryId, Category category) throws BankBadRequestException;

    void removeCategoryWithAllTransactions(Integer userId, Integer categoryId) throws BankResourceNotFoundException;
}
