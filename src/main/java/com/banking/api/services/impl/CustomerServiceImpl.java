package com.banking.api.services.impl;

import com.banking.api.domain.Account;
import com.banking.api.domain.Customer;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;
import com.banking.api.repositories.CustomerRepository;
import com.banking.api.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public List<Customer> fetchAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer fetchCustomerById(Long id) throws BankResourceNotFoundException {
        return customerRepository.findById(id);
    }

    @Override
    public Customer addCustomer(Long userId, String firstName, String lastName, String middleInitial,
                                String email, String mobilePhoneNumber, String phoneNumber, String zipCode,
                                String address, String state, String city, String country, String ssn,
                                Timestamp createdDate) throws BankBadRequestException {
        Long id = customerRepository.create(userId, firstName, lastName, middleInitial, email, mobilePhoneNumber,
                phoneNumber, zipCode, address, state, city, country, ssn, createdDate);
        return customerRepository.findById(id);
    }

    @Override
    public void updateCustomer(Long id, Customer customer) throws BankBadRequestException {
        customerRepository.update(id, customer);
    }

    @Override
    public void removeCustomer(Long id) throws BankResourceNotFoundException {
        this.fetchCustomerById(id);
        customerRepository.removeById(id);
    }

    @Override
    public List<Account> fetchAllCustomersAccount(Long userId) {
        return customerRepository.findAllAccounts(userId);
    }
}
