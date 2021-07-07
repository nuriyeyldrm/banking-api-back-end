package com.banking.api.repositories.impl;

import com.banking.api.domain.Employee;
import com.banking.api.domain.User;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;
import com.banking.api.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private static final String SQL_CREATE = "INSERT INTO employees (id, user_id, first_name, last_name, " +
            "email, hired_date, mobile_phone_number, phone_number, zip_code, address, state, city, country," +
            "ssn, created_date) " +
            "VALUES(NEXTVAL('EMPLOYEES_SEQ'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT id, user_id, first_name, last_name, email, hired_date, " +
            "mobile_phone_number, phone_number, zip_code, address, state, city, country, ssn, created_date " +
            "FROM employees WHERE id = ?";

    private static final String SQL_FIND_ALL = "SELECT id, user_id, first_name, last_name, email, hired_date, " +
            "mobile_phone_number, phone_number, zip_code, address, state, city, country, ssn, created_date " +
            "FROM employees";

    private static final String SQL_UPDATE = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, " +
            "mobile_phone_number = ?, phone_number = ?, zip_code = ?, address = ?, state = ?, city = ?, " +
            "country = ?, ssn = ? WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM employees WHERE id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Employee> findAll() throws BankResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL, employeeRowMapper);
    }

    @Override
    public Employee findById(Long id) throws BankResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, employeeRowMapper, id);
        }catch (Exception e){
            throw new BankResourceNotFoundException("Employee not found");
        }
    }

    @Override
    public Long create(Long userId, String firstName, String lastName, String email, Timestamp hiredDate,
                       String mobilePhoneNumber, String phoneNumber, String zipCode, String address, String state,
                       String city, String country, String ssn, Timestamp createdDate) throws BankBadRequestException {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, userId);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setString(4, email);
                ps.setTimestamp(5, hiredDate);
                ps.setString(6, mobilePhoneNumber);
                ps.setString(7, phoneNumber);
                ps.setString(8, zipCode);
                ps.setString(9, address);
                ps.setString(10, state);
                ps.setString(11, city);
                ps.setString(12, country);
                ps.setString(13, ssn);
                ps.setTimestamp(14, createdDate);
                return ps;
            }, keyHolder);
            return (Long) keyHolder.getKeys().get("id");
        }catch (Exception e){
            throw new BankBadRequestException("Invalid Request");
        }
    }

    @Override
    public void update(Long id, Employee employee) throws BankBadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE, employee.getFirstName(), employee.getLastName(), employee.getEmail(),
                    employee.getMobilePhoneNumber(), employee.getPhoneNumber(), employee.getZipCode(),
                    employee.getAddress(), employee.getState(), employee.getCity(), employee.getCountry(),
                    employee.getSsn(), id);
        }catch (Exception e){
            throw new BankBadRequestException("Invalid request");
        }
    }

    @Override
    public void removeById(Long id) {
        int count = jdbcTemplate.update(SQL_DELETE, id);
        if (count == 0)
            throw new BankResourceNotFoundException("Employee not found");
    }

    private RowMapper<Employee> employeeRowMapper = ((rs, rowNum) -> {
        return new Employee(rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getTimestamp("hired_date"),
                rs.getString("mobile_phone_number"),
                rs.getString("phone_number"),
                rs.getString("zip_code"),
                rs.getString("address"),
                rs.getString("state"),
                rs.getString("city"),
                rs.getString("country"),
                rs.getString("ssn"),
                rs.getTimestamp("created_date"));
    });
}
