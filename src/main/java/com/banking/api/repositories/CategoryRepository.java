package com.banking.api.repositories;

import com.banking.api.domain.Category;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;

import java.util.List;

public interface CategoryRepository {

    List<Category> findAll(Integer userId) throws BankResourceNotFoundException;

    Category findById(Integer userId, Integer categoryId) throws BankResourceNotFoundException;

    Integer create(Integer userId, String title, String description) throws BankBadRequestException;

    void update(Integer userId, Integer categoryId, Category category) throws BankBadRequestException;

    void removeById(Integer userId, Integer categoryId);
}
