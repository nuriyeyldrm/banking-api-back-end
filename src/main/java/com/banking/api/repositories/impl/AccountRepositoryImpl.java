package com.banking.api.repositories.impl;

import com.banking.api.domain.Account;
import com.banking.api.domain.enumeration.AccountStatusType;
import com.banking.api.domain.enumeration.AccountType;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;
import com.banking.api.repositories.AccountRepository;
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
public class AccountRepositoryImpl implements AccountRepository {

    private static final String SQL_CREATE = "INSERT INTO accounts (id, user_id, description, balance, account_type, " +
            "account_status_type, created_date) VALUES(NEXTVAL('ACCOUNTS_SEQ'), ?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_ALL = "SELECT id, description, balance, account_type, account_status_type, " +
            "created_date FROM accounts WHERE user_id = ?";


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Account> findAll(Integer userId) throws BankResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL, accountRowMapper, userId);
    }

    @Override
    public Account findById(Integer userId, Long id) throws BankResourceNotFoundException {
        return null;
    }

    @Override
    public Long create(Integer userId, String description, Integer balance, String accountType,
                       String accountStatusType, Timestamp createdDate) throws BankBadRequestException {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setString(2, description);
                ps.setInt(3, balance);
                ps.setString(4, accountType);
                ps.setString(5, accountStatusType);
                ps.setTimestamp(6, createdDate);
                return ps;
            }, keyHolder);
            return (Long) keyHolder.getKeys().get("id");
        }catch (Exception e){
            throw new BankBadRequestException("Invalid request");
        }
    }

    @Override
    public void update(Integer userId, Long id, Account account) throws BankBadRequestException {

    }

    @Override
    public void removeById(Integer userId, Long id) {

    }

    private RowMapper<Account> accountRowMapper = ((rs, rowNum) -> {
        return new Account(rs.getLong("id"),
                rs.getInt("user_id"),
                rs.getString("description"),
                rs.getInt("balance"),
                rs.getString("account_type"),
                rs.getString("account_status_type"),
                rs.getTimestamp("created_date"));
    });
}
