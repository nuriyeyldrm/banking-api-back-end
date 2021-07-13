package com.banking.api.repositories.impl;

import com.banking.api.domain.Account;
import com.banking.api.domain.Transfer;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;
import com.banking.api.repositories.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransferRepositoryImpl implements TransferRepository {

    private static final String SQL_CREATE = "INSERT INTO transfers (id, from_account_id, to_account_id, user_id, " +
            "transaction_amount, new_balance, currency_code, transaction_date, description) " +
            "VALUES(NEXTVAL('sequence_generator'), ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT id, from_account_id, to_account_id, user_id, transaction_amount, " +
            "new_balance, currency_code, transaction_date, description " +
            "FROM transfers WHERE user_id = ? AND id = ?";

    private static final String SQL_FIND_ALL = "SELECT id, from_account_id, to_account_id, user_id, transaction_amount, " +
            "new_balance, currency_code, transaction_date, description " +
            "FROM transfers WHERE user_id = ?";

    private static final String SQL_FIND_BY_ID_ACC_TO = "SELECT id, user_id, description, balance, account_type, " +
            "account_status_type, created_date FROM accounts WHERE id = ?";

    private static final String SQL_FIND_BY_ID_ACC_FROM = "SELECT id, user_id, description, balance, account_type, " +
            "account_status_type, created_date FROM accounts WHERE id = ? AND user_id = ?";

    private static final String SQL_UPDATE_ACC_BALANCE = "UPDATE accounts SET balance = ? WHERE id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public List<Transfer> findAll(Long userId) throws BankResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL, transferRowMapper, userId);
    }

    @Override
    public Transfer findById(Long userId, Long id) throws BankResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, transferRowMapper, userId, id);
        }catch (Exception e){
            throw new BankResourceNotFoundException("Transfer not found");
        }
    }

    @Override
    public Long create(Long fromAccountId, Long toAccountId, Long userId, Double transactionAmount,
                       String currencyCode, Timestamp transactionDate, String description)
            throws BankBadRequestException {

        Double newBalance = transferMoney(transactionAmount, fromAccountId, toAccountId, userId);

        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, fromAccountId);
                ps.setLong(2, toAccountId);
                ps.setLong(3, userId);
                ps.setDouble(4, transactionAmount);
                ps.setDouble(5, newBalance);
                ps.setString(6, currencyCode);
                ps.setTimestamp(7, transactionDate);
                ps.setString(8, description);

                return ps;
            }, keyHolder);
            return (Long) keyHolder.getKeys().get("id");
        }catch (Exception e){
            throw new BankBadRequestException("Invalid Request");
        }
    }

    public Double transferMoney(Double transactionAmount, Long fromAccountId, Long toAccountId, Long userId) {

        List<Account> accounts = findAccountById(fromAccountId, toAccountId, userId);

        Account fromAccount = accounts.get(0);
        Account toAccount = accounts.get(1);

        if (transactionAmount > fromAccount.getBalance()){
            throw new BankBadRequestException("not enough funds available for transfer");
        }

        final Double newFromBalance = fromAccount.getBalance() - transactionAmount;

        final Double newToBalance = toAccount.getBalance() + transactionAmount;

        fromAccount.setBalance(newFromBalance);
        toAccount.setBalance(newToBalance);

        jdbcTemplate.update(SQL_UPDATE_ACC_BALANCE, fromAccount.getBalance(), fromAccountId);
        jdbcTemplate.update(SQL_UPDATE_ACC_BALANCE, toAccount.getBalance(), toAccountId);

        return newFromBalance;

    }

    public List<Account> findAccountById(Long fromAccountId, Long toAccountId, Long userId)
            throws BankResourceNotFoundException{
        try{
            Account fromAccount = jdbcTemplate.queryForObject(SQL_FIND_BY_ID_ACC_FROM, accountRowMapper,
                    fromAccountId, userId);

            Account toAccount = jdbcTemplate.queryForObject(SQL_FIND_BY_ID_ACC_TO, accountRowMapper,
                    toAccountId);
            List<Account> accounts = new ArrayList<>();
            accounts.add(fromAccount);
            accounts.add(toAccount);
            return accounts;
        }catch (Exception e){
            throw new BankResourceNotFoundException("account not found");
        }
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
    private RowMapper<Transfer> transferRowMapper = ((rs, rowNum) -> {
        return new Transfer(rs.getLong("id"),
                rs.getLong("from_account_id"),
                rs.getLong("to_account_id"),
                rs.getLong("user_id"),
                rs.getDouble("transaction_amount"),
                rs.getDouble("new_balance"),
                rs.getString("currency_code"),
                rs.getTimestamp("transaction_date"),
                rs.getString("description"));
    });
}
