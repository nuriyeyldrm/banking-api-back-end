package com.banking.api.repositories;

import com.banking.api.domain.Account;
import com.banking.api.domain.enumeration.AccountStatusType;
import com.banking.api.domain.enumeration.AccountType;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;

import java.sql.Timestamp;
import java.util.List;

public interface AccountRepository {

    List<Account> findAll(Long userId) throws BankResourceNotFoundException;

    Account findById(Long userId, Long id) throws BankResourceNotFoundException;

    Long create(Long userId, String description, Double balance, String accountType,
                String accountStatusType, Timestamp createdDate) throws BankBadRequestException;

    void update(Long userId, Long id, Account account) throws BankBadRequestException;

    void removeById(Long userId, Long id);
}
