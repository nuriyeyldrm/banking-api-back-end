package com.banking.api.services.impl;

import com.banking.api.domain.Employee;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;
import com.banking.api.repositories.EmployeeRepository;
import com.banking.api.services.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public List<Employee> fetchAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee fetchEmployeeById(Long id) throws BankResourceNotFoundException {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee addEmployee(Long userId, String firstName, String lastName, String email, Timestamp hiredDate,
                                String mobilePhoneNumber, String phoneNumber, String zipCode, String address,
                                String state, String city, String country, String ssn, Timestamp createdDate)
            throws BankBadRequestException {
        Long id = employeeRepository.create(userId, firstName, lastName, email, hiredDate, mobilePhoneNumber,
                phoneNumber, zipCode, address, state, city, country, ssn, createdDate);
        return employeeRepository.findById(id);
    }

    @Override
    public void updateEmployee(Long id, Employee employee) throws BankBadRequestException {
        employeeRepository.update(id, employee);
    }

    @Override
    public void removeEmployee(Long id) throws BankResourceNotFoundException {
        this.fetchEmployeeById(id);
        employeeRepository.removeById(id);
    }
}
