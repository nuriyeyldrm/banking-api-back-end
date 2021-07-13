package com.banking.api.repositories.impl;

import com.banking.api.domain.User;
import com.banking.api.exceptions.BankAuthException;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;
import com.banking.api.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_CREATE = "INSERT INTO users(id, first_name, last_name, email, password_hash, " +
            "created_by, created_date, last_modified_by, last_modified_date) " +
            "VALUES(NEXTVAL('SEQUENCE_GENERATOR'), ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM users WHERE email = ?";

    private static final String SQL_FIND_BY_ID = "SELECT id, first_name, last_name, email, password_hash, " +
            "created_by, created_date, last_modified_by, last_modified_date " +
            "FROM users WHERE id = ?";

    private static final String SQL_FIND_BY_EMAIL = "SELECT id, first_name, last_name, email, password_hash, " +
            "created_by, created_date, last_modified_by, last_modified_date " +
            "FROM users WHERE email = ?";

    private static final String SQL_FIND_ALL = "SELECT id, first_name, last_name, email, password_hash, " +
            "created_by, created_date, last_modified_by, last_modified_date " +
            "FROM users";

    private static final String SQL_UPDATE = "UPDATE users SET first_name = ?, last_name = ?, email = ?, " +
            "password_hash = ?, last_modified_by = ?, last_modified_date = ? " +
            "WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM users WHERE id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Long create(String firstName, String lastName, String email, String password, String createdBy,
                       Timestamp createdDate, String lastModifiedBy, Timestamp lastModifiedDate) throws BankAuthException {

        // If user enter same password hashes will be different
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, hashedPassword);
                ps.setString(5, createdBy);
                ps.setTimestamp(6, createdDate);
                ps.setString(7, lastModifiedBy);
                ps.setTimestamp(8, lastModifiedDate);
                return ps;
            }, keyHolder);
            return (Long) keyHolder.getKeys().get("id");
        }catch (Exception e) {
            throw new BankAuthException("Invalid details. Failed to create account");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws BankAuthException {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, userRowMapper, email);
            if (!BCrypt.checkpw(password, user.getPassword()))
                throw new BankAuthException("Invalid email/password");
            return user;
        }catch (EmptyResultDataAccessException e){
            throw new BankAuthException("Invalid email/password");
        }
    }

    @Override
    public Integer getCountByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, Integer.class, email);
    }

    @Override
    public User findById(Long id) throws BankResourceNotFoundException {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, userRowMapper, id);
    }

    @Override
    public List<User> findAll() throws BankResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL, userRowMapper);
    }

    @Override
    public void update(Long id, User user) throws BankBadRequestException {
        try {
            Date date= new Date();
            long time = date.getTime();
            Timestamp createdDate = new Timestamp(time);
            user.setLastModifiedDate(createdDate);
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
            jdbcTemplate.update(SQL_UPDATE, user.getFirstname(), user.getLastname(), user.getEmail(), hashedPassword,
                    user.getLastModifiedBy(), user.getLastModifiedDate(), id);
        }catch (Exception e){
            throw new BankBadRequestException("Invalid request");
        }
    }

    @Override
    public void removeById(Long id) throws BankResourceNotFoundException {
        int count = jdbcTemplate.update(SQL_DELETE, id);
        if (count == 0)
            throw new BankResourceNotFoundException("User not found");
    }

    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
       return new User(rs.getLong("id"),
               rs.getString("first_name"),
               rs.getString("last_name"),
               rs.getString("email"),
               rs.getString("password_hash"),
               rs.getString("created_by"),
               rs.getTimestamp("created_date"),
               rs.getString("last_modified_by"),
               rs.getTimestamp("last_modified_date"));
    });
}
