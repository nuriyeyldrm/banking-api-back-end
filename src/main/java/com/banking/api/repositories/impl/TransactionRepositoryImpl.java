package com.banking.api.repositories.impl;

import com.banking.api.domain.Transaction;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;
import com.banking.api.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private static final String SQL_CREATE = "INSERT INTO transactions(transaction_id, category_id, user_id, " +
            "amount, note, transaction_date) VALUES(NEXTVAL('sequence_generator'), ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT transaction_id, category_id, user_id, amount, note, " +
            "transaction_date FROM transactions WHERE user_id = ? AND category_id = ? AND transaction_id = ?";

    private static final String SQL_FIND_ALL = "SELECT transaction_id, category_id, user_id, amount, note, " +
            "transaction_date FROM transactions WHERE user_id = ? AND category_id = ?";

    private static final String SQL_UPDATE = "UPDATE transactions SET amount = ?, note = ?, transaction_date = ? " +
            "WHERE user_id = ? AND category_id = ? AND transaction_id = ?";

    private static final String SQL_DELETE = "DELETE FROM transactions WHERE user_id = ? AND category_id = ? AND " +
            "transaction_id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Transaction> findAll(Integer userId, Integer categoryId) {

        return jdbcTemplate.query(SQL_FIND_ALL, transactionRowMapper, userId, categoryId);
    }

    @Override
    public Transaction findById(Integer userId, Integer categoryId, Integer transactionId)
            throws BankResourceNotFoundException {
        try{
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, transactionRowMapper, userId, categoryId, transactionId);
        }catch (Exception e){
            throw new BankResourceNotFoundException("Transaction not found");
        }
    }

    @Override
    public Integer create(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate)
            throws BankBadRequestException {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, categoryId);
                ps.setInt(2, userId);
                ps.setDouble(3, amount);
                ps.setString(4, note);
                ps.setLong(5, transactionDate);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("transaction_id");
        }catch (Exception e){
           throw new BankBadRequestException("Invalid request");
        }
    }

    @Override
    public void update(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction)
            throws BankBadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE, transaction.getAmount(), transaction.getNote(),
                    transaction.getTransactionDate(), userId, categoryId, transactionId);
        }catch (Exception e){
            throw new BankBadRequestException("Invalid request");
        }
    }

    @Override
    public void removeById(Integer userId, Integer categoryId, Integer transactionId)
            throws BankResourceNotFoundException {
        int count = jdbcTemplate.update(SQL_DELETE, userId, categoryId, transactionId);
        if(count == 0)
            throw new BankResourceNotFoundException("Transaction not found");
    }

    private RowMapper<Transaction> transactionRowMapper = ((rs, rowNum) -> {
        return new Transaction(rs.getInt("transaction_id"),
                rs.getInt("category_id"),
                rs.getInt("user_id"),
                rs.getDouble("amount"),
                rs.getString("note"),
                rs.getLong("transaction_date"));
    });
}
