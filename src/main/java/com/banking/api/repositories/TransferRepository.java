package com.banking.api.repositories;

import com.banking.api.domain.Transfer;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;

import java.sql.Timestamp;

public interface TransferRepository {

    Transfer findById(Long userId, Long id) throws BankResourceNotFoundException;

    Long create(Long fromAccountId, Long toAccountId, Long userId, Double transactionAmount,
                String currencyCode, Timestamp transactionDate, String description)
            throws BankBadRequestException;
}
