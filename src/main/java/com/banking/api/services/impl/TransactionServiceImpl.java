package com.banking.api.services.impl;

import com.banking.api.domain.Transaction;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;
import com.banking.api.repositories.TransactionRepository;
import com.banking.api.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<Transaction> fetchAllTransactions(Integer userId, Integer categoryId) {
        return transactionRepository.findAll(userId, categoryId);
    }

    @Override
    public Transaction fetchTransactionById(Integer userId, Integer categoryId, Integer transactionId) throws BankResourceNotFoundException {
        return transactionRepository.findById(userId, categoryId, transactionId);
    }

    @Override
    public Transaction addTransaction(Integer userId, Integer categoryId, Double amount, String note,
                                      Long transactionDate) throws BankBadRequestException {
        int transactionId = transactionRepository.create(userId, categoryId, amount, note, transactionDate);
        return transactionRepository.findById(userId, categoryId, transactionId);
    }

    @Override
    public void updateTransaction(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction)
            throws BankBadRequestException {
        transactionRepository.update(userId, categoryId, transactionId, transaction);
    }

    @Override
    public void removeTransaction(Integer userId, Integer categoryId, Integer transactionId)
            throws BankResourceNotFoundException {
        transactionRepository.removeById(userId, categoryId, transactionId);
    }
}
