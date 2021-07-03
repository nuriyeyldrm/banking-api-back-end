package com.banking.api.services;

import com.banking.api.domain.Transaction;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;

import java.util.List;

public interface TransactionService {

    List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId);

    Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId)
            throws BankResourceNotFoundException;

    Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate)
            throws BankBadRequestException;

    void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction)
            throws BankBadRequestException;

    void removeTransaction(Integer userId, Integer categoryId, Integer transactionId)
            throws BankResourceNotFoundException;
}
