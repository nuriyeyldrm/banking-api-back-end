package com.banking.api.services;

import com.banking.api.domain.Account;
import com.banking.api.domain.enumeration.AccountStatusType;
import com.banking.api.domain.enumeration.AccountType;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;

import java.sql.Timestamp;
import java.util.List;

public interface AccountService {

    List<Account> fetchAllAccounts(Long userId);

    Account fetchAccountById(Long userId, Long id) throws BankResourceNotFoundException;

    Account addAccount(Long userId, String description, Double balance, String accountType,
                       String accountStatusType, Timestamp createdDate) throws BankBadRequestException;

    void updateAccount(Long userId, Long id, Account account) throws BankBadRequestException;

    void removeAccount(Long userId, Long id) throws BankResourceNotFoundException;

}
