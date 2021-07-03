package com.banking.api.repositories;

import com.banking.api.domain.Account;
import com.banking.api.domain.enumeration.AccountStatusType;
import com.banking.api.domain.enumeration.AccountType;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;

import java.sql.Timestamp;
import java.util.List;

public interface AccountRepository {

    List<Account> findAll(Integer userId) throws BankResourceNotFoundException;

    Account findById(Integer userId, Long id) throws BankResourceNotFoundException;

    Long create(Integer userId, String description, Integer balance, String accountType,
                String accountStatusType, Timestamp createdDate) throws BankBadRequestException;

    void update(Integer userId, Long id, Account account) throws BankBadRequestException;

    void removeById(Integer userId, Long id);
}
