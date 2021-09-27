package com.banking.api.services.impl;

import com.banking.api.domain.Account;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;
import com.banking.api.repositories.AccountRepository;
import com.banking.api.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public List<Account> fetchAllAccounts(Long userId) {
        return accountRepository.findAll(userId);
    }

    @Override
    public Account fetchAccountById(Long userId, Long id) throws BankResourceNotFoundException {
        return accountRepository.findById(userId, id);
    }

    @Override
    public Account addAccount(Long userId, String description, Double balance, String accountType,
                              String accountStatusType, Timestamp createdDate)
            throws BankBadRequestException {
        Long id = accountRepository.create(userId, description, balance, accountType, accountStatusType, createdDate);
        return accountRepository.findById(userId, id);
    }

    @Override
    public void updateAccount(Long userId, Long id, Account account) throws BankBadRequestException {
        accountRepository.update(userId, id, account);
    }

    @Override
    public void removeAccount(Long userId, Long id) throws BankResourceNotFoundException {
        this.fetchAccountById(userId, id);
        accountRepository.removeById(userId, id);
    }
}
