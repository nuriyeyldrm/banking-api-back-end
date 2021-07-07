package com.banking.api.services;

import com.banking.api.domain.Account;
import com.banking.api.domain.Customer;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;

import java.sql.Timestamp;
import java.util.List;

public interface CustomerService {

    List<Customer> fetchAllCustomers();

    Customer fetchCustomerById(Long id) throws BankResourceNotFoundException;

    Customer addCustomer(Long userId, String firstName, String lastName, String middleInitial, String email,
                         String mobilePhoneNumber, String phoneNumber, String zipCode, String address, String state,
                         String city, String country, String ssn, Timestamp createdDate) throws BankBadRequestException;

    void updateCustomer(Long id, Customer customer) throws BankBadRequestException;

    void removeCustomer(Long id) throws BankResourceNotFoundException;

    List<Account> fetchAllCustomersAccount(Long userId);
}
