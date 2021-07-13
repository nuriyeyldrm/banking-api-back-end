package com.banking.api.repositories.impl;

import com.banking.api.domain.Category;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;
import com.banking.api.repositories.CategoryRepository;
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
public class CategoryRepositoryImpl implements CategoryRepository {

    private static final String SQL_CREATE = "INSERT INTO categories (category_id, user_id, title, description) " +
            "VALUES(NEXTVAL('sequence_generator'), ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT c.category_id, c.user_id, c.title, c.description, " +
            "COALESCE(SUM(t.amount), 0) total_expense " +
            "FROM transactions t RIGHT OUTER JOIN categories c ON c.category_id = t.category_id " +
            "WHERE c.user_id = ? AND c.category_id = ? GROUP BY c.category_id";

    private static final String SQL_FIND_ALL = "SELECT c.category_id, c.user_id, c.title, c.description, " +
            "COALESCE(SUM(t.amount), 0) total_expense " +
            "FROM transactions t RIGHT OUTER JOIN categories c ON c.category_id = t.category_id " +
            "WHERE c.user_id = ? GROUP BY c.category_id";

    private static final String SQL_UPDATE = "UPDATE categories SET title = ?, description = ? " +
            "WHERE user_id = ? AND category_id = ?";

    private static final String SQL_DELETE_CATEGORY = "DELETE FROM categories WHERE user_id = ? AND " +
            "category_id = ?";

    private static final String SQL_DELETE_ALL_TRANSACTIONS = "DELETE FROM transactions WHERE category_id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Category> findAll(Integer userId) throws BankResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL, categoryRowMapper, userId);
    }

    @Override
    public Category findById(Integer userId, Integer categoryId) throws BankResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, categoryRowMapper, userId, categoryId);
        }catch (Exception e){
            throw new BankResourceNotFoundException("Category not found");
        }
    }

    @Override
    public Integer create(Integer userId, String title, String description) throws BankBadRequestException {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setString(2, title);
                ps.setString(3, description);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("category_id");
        }catch (Exception e){
            throw new BankBadRequestException("Invalid Request");
        }
    }

    @Override
    public void update(Integer userId, Integer categoryId, Category category) throws BankBadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE, category.getTitle(), category.getDescription(), userId, categoryId);
        }catch (Exception e){
            throw new BankBadRequestException("Invalid request");
        }
    }

    @Override
    public void removeById(Integer userId, Integer categoryId) {
        this.removeAllCatTransactions(categoryId);
        jdbcTemplate.update(SQL_DELETE_CATEGORY, userId, categoryId);
    }

    private void removeAllCatTransactions(Integer categoryId){
        jdbcTemplate.update(SQL_DELETE_ALL_TRANSACTIONS, categoryId);
    }

    private RowMapper<Category> categoryRowMapper = ((rs, rowNum) -> {
        return new Category(rs.getInt("category_id"),
                rs.getInt("user_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getDouble("total_expense"));
    });
}
