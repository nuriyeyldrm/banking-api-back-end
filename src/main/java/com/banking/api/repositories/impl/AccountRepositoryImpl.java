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
            "account_status_type, created_date) VALUES(NEXTVAL('sequence_generator'), ?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT id, user_id, description, balance, account_type, " +
            "account_status_type, created_date FROM accounts WHERE user_id = ? AND id = ?";

    private static final String SQL_FIND_ALL = "SELECT id, user_id, description, balance, account_type, " +
            "account_status_type, created_date FROM accounts WHERE user_id = ?";

    private static final String SQL_UPDATE = "UPDATE accounts SET description = ?, balance = ?, account_type = ?, " +
            "account_status_type = ? WHERE user_id = ? AND id = ?";

    private static final String SQL_DELETE = "DELETE FROM accounts WHERE user_id = ? AND " +
            "id = ?";




    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Account> findAll(Long userId) throws BankResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL, accountRowMapper, userId);
    }

    @Override
    public Account findById(Long userId, Long id) throws BankResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, accountRowMapper, userId, id);
        }catch (Exception e){
            throw new BankResourceNotFoundException("Account not found");
        }
    }

    @Override
    public Long create(Long userId, String description, Double balance, String accountType,
                       String accountStatusType, Timestamp createdDate) throws BankBadRequestException {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, userId);
                ps.setString(2, description);
                ps.setDouble(3, balance);
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
    public void update(Long userId, Long id, Account account) throws BankBadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE, account.getDescription(), account.getBalance(), account.getAccountType(),
                    account.getAccountStatusType(), userId, id);
        }catch (Exception e){
            throw new BankBadRequestException("Invalid request");
        }
    }

    @Override
    public void removeById(Long userId, Long id) {
        int count = jdbcTemplate.update(SQL_DELETE, userId, id);
        if(count == 0)
            throw new BankResourceNotFoundException("Account not found");
    }

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
