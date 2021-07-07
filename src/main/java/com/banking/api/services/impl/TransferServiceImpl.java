package com.banking.api.services.impl;

import com.banking.api.domain.Transfer;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;
import com.banking.api.repositories.TransferRepository;
import com.banking.api.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class TransferServiceImpl implements TransferService {

    @Autowired
    TransferRepository transferRepository;

    @Override
    public List<Transfer> fetchAllTransfers(Long userId) {
        return transferRepository.findAll(userId);
    }

    @Override
    public Transfer fetchTransferById(Long userId, Long id) throws BankResourceNotFoundException {
        return transferRepository.findById(userId, id);
    }

    @Override
    public Transfer addTransfer(Long fromAccountId, Long toAccountId, Long userId, Double transactionAmount,
                               String currencyCode, Timestamp transactionDate, String description)
            throws BankBadRequestException {

        Long id = transferRepository.create(fromAccountId, toAccountId, userId, transactionAmount,
                currencyCode, transactionDate, description);
        return transferRepository.findById(userId, id);
    }
}
