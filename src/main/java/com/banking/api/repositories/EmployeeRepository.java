package com.banking.api.repositories;

import com.banking.api.domain.Employee;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;

import java.sql.Timestamp;
import java.util.List;

public interface EmployeeRepository {

    List<Employee> findAll() throws BankResourceNotFoundException;

    Employee findById(Long id) throws BankResourceNotFoundException;

    Long create(Long userId, String firstName, String lastName, String email, Timestamp hiredDate,
                String mobilePhoneNumber, String phoneNumber, String zipCode, String address, String state,
                String city, String country, String ssn, Timestamp createdDate) throws BankBadRequestException;

    void update(Long id, Employee employee) throws BankBadRequestException;

    void removeById(Long id);
}
