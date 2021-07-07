package com.banking.api.services;

import com.banking.api.domain.Transfer;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;

import java.sql.Timestamp;
import java.util.List;

public interface TransferService {

    List<Transfer> fetchAllTransfers(Long userId);

    Transfer fetchTransferById(Long userId, Long id) throws BankResourceNotFoundException;

    Transfer addTransfer(Long fromAccountId, Long toAccountId, Long userId, Double transactionAmount,
                         String currencyCode, Timestamp transactionDate, String description)
            throws BankBadRequestException;
}
