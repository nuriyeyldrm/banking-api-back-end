package com.banking.api.repositories;

import com.banking.api.domain.Transaction;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;

import java.util.List;

public interface TransactionRepository {

    List<Transaction> findAll(Integer userId, Integer categoryId);

    Transaction findById(Integer userId, Integer categoryId, Integer transactionId)
            throws BankResourceNotFoundException;

    Integer create(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate)
        throws BankBadRequestException;

    void update(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction)
            throws BankBadRequestException;

    void removeById(Integer userId, Integer categoryId, Integer transactionId) throws BankResourceNotFoundException;
}
