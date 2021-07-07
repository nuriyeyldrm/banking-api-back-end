package com.banking.api.repositories;

import com.banking.api.domain.Account;
import com.banking.api.domain.Customer;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;

import java.sql.Timestamp;
import java.util.List;

public interface CustomerRepository {

    List<Customer> findAll() throws BankResourceNotFoundException;

    Customer findById(Long id) throws BankResourceNotFoundException;

    Long create(Long userId, String firstName, String lastName, String middleInitial, String email,
                String mobilePhoneNumber, String phoneNumber, String zipCode, String address, String state, String city,
                String country, String ssn, Timestamp createdDate) throws BankBadRequestException;

    void update(Long id, Customer customer) throws BankBadRequestException;

    void removeById(Long id);

    List<Account> findAllAccounts(Long userId) throws BankResourceNotFoundException;
}
