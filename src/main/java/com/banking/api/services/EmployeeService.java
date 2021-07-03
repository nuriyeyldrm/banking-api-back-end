package com.banking.api.services;

import com.banking.api.domain.Employee;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;

import java.sql.Timestamp;
import java.util.List;

public interface EmployeeService {

    List<Employee> fetchAllEmployees();

    Employee fetchEmployeeById(Long id) throws BankResourceNotFoundException;

    Employee addEmployee(Long userId, String firstName, String lastName, String email, Timestamp hiredDate,
                         String mobilePhoneNumber, String phoneNumber, String zipCode, String address, String state,
                         String city, String country, String ssn, Timestamp createdDate) throws BankBadRequestException;

    void updateEmployee(Long id, Employee employee) throws BankBadRequestException;

    void removeEmployee(Long id) throws BankResourceNotFoundException;
}
