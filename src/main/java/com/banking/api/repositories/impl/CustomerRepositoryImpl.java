package com.banking.api.repositories.impl;

import com.banking.api.domain.Account;
import com.banking.api.domain.Customer;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;
import com.banking.api.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private static final String SQL_CREATE = "INSERT INTO customers (id, user_id, first_name, last_name, " +
            "middle_initial, email, mobile_phone_number, phone_number, zip_code, address, state, city, country," +
            "ssn, created_date) " +
            "VALUES(NEXTVAL('sequence_generator'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT id, user_id, first_name, last_name, middle_initial, " +
            "email, mobile_phone_number, phone_number, zip_code, address, state, country, city, ssn, created_date " +
            "FROM customers WHERE id = ?";

    private static final String SQL_FIND_ALL = "SELECT id, user_id, first_name, last_name, middle_initial, email, " +
            "mobile_phone_number, phone_number, zip_code, address, state, city, country, ssn, created_date " +
            "FROM customers";

    private static final String SQL_FIND_ALL_CST_ACC = "SELECT id, user_id, description, balance, account_type, " +
            "account_status_type, created_date FROM accounts WHERE user_id = ?";

    private static final String SQL_UPDATE = "UPDATE customers SET first_name = ?, last_name = ?, middle_initial = ?, " +
            "email = ?, mobile_phone_number = ?, phone_number = ?, zip_code = ?, address = ?, state = ?, city = ?, " +
            "country = ?, ssn = ?, created_date = ? WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM customers WHERE id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Customer> findAll() throws BankResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL, customerRowMapper);
    }

    @Override
    public Customer findById(Long id) throws BankResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, customerRowMapper, id);
        }catch (Exception e){
            throw new BankResourceNotFoundException("Customer not found");
        }
    }

    @Override
    public Long create(Long userId, String firstName, String lastName, String middleInitial, String email,
                       String mobilePhoneNumber, String phoneNumber, String zipCode, String address, String state,
                       String city, String country, String ssn, Timestamp createdDate)
            throws BankBadRequestException {

        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, userId);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setString(4, middleInitial);
                ps.setString(5, email);
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
    public void update(Long id, Customer customer) throws BankBadRequestException {
        try {
            Date date= new Date();
            long time = date.getTime();
            Timestamp createdDate = new Timestamp(time);
            customer.setCreatedDate(createdDate);
            jdbcTemplate.update(SQL_UPDATE, customer.getFirstName(), customer.getLastName(), customer.getMiddleInitial(),
                    customer.getEmail(), customer.getMobilePhoneNumber(), customer.getPhoneNumber(), customer.getZipCode(),
                    customer.getAddress(), customer.getState(), customer.getCity(), customer.getCountry(), customer.getSsn(),
                    customer.getCreatedDate(), id);
        }catch (Exception e){
            throw new BankBadRequestException("Invalid request");
        }
    }

    @Override
    public void removeById(Long id) {
        int count = jdbcTemplate.update(SQL_DELETE, id);
        if (count == 0)
            throw new BankResourceNotFoundException("Customer not found");
    }

    @Override
    public List<Account> findAllAccounts(Long userId) throws BankResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL_CST_ACC, accountRowMapper, userId);
    }

    private RowMapper<Customer> customerRowMapper = ((rs, rowNum) -> {
        return new Customer(rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("middle_initial"),
                rs.getString("email"),
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

    private RowMapper<Account> accountRowMapper = ((rs, rowNum) -> {
        return new Account(rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getString("description"),
                rs.getDouble("balance"),
                rs.getString("account_type"),
                rs.getString("account_status_type"),
                rs.getTimestamp("created_date"));
    });

}
