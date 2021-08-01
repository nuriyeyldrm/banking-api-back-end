package com.banking.api.repositories.impl;

import com.banking.api.domain.User;
import com.banking.api.domain.enumeration.AppUserRole;
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

    private static final String SQL_CREATE = "INSERT INTO users(id, ssn, first_name, last_name, email, password_hash, " +
            "address, mobile_phone_number, created_by, created_date, last_modified_by, last_modified_date, " +
            "app_user_role) VALUES(NEXTVAL('sequence_generator'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM users WHERE email = ?";

    private static final String SQL_COUNT_BY_SSN = "SELECT COUNT(*) FROM users WHERE ssn = ?";

    private static final String SQL_FIND_BY_ID = "SELECT id, ssn, first_name, last_name, email, password_hash, " +
            "address, mobile_phone_number, created_by, created_date, last_modified_by, last_modified_date, " +
            "app_user_role FROM users WHERE id = ?";

    private static final String SQL_FIND_BY_SSN = "SELECT id, ssn, first_name, last_name, email, password_hash, " +
            "address, mobile_phone_number, created_by, created_date, last_modified_by, last_modified_date, " +
            "app_user_role FROM users WHERE ssn = ?";

    private static final String SQL_FIND_ALL = "SELECT id, ssn, first_name, last_name, email, password_hash, " +
            "address, mobile_phone_number, created_by, created_date, last_modified_by, last_modified_date, " +
            "app_user_role FROM users";

    private static final String SQL_UPDATE = "UPDATE users SET ssn = ?, first_name = ?, last_name = ?, email = ?, " +
            "address = ?, mobile_phone_number = ?, last_modified_by = ?, last_modified_date = ? WHERE id = ?";

    private static final String SQL_UPDATE_PASSWORD = "UPDATE users SET  password_hash = ? WHERE id = ?";

    private static final String SQL_DELETE = "DELETE FROM users WHERE id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Long create(String ssn, String firstName, String lastName, String email, String password, String address,
                       String mobilePhoneNumber, String createdBy, Timestamp createdDate, String lastModifiedBy,
                       Timestamp lastModifiedDate) throws BankBadRequestException {

        // If user enter same password hashes will be different
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, ssn);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setString(4, email);
                ps.setString(5, hashedPassword);
                ps.setString(6, address);
                ps.setString(7, mobilePhoneNumber);
                ps.setString(8, createdBy);
                ps.setTimestamp(9, createdDate);
                ps.setString(10, lastModifiedBy);
                ps.setTimestamp(11, lastModifiedDate);
                ps.setString(12, String.valueOf(AppUserRole.USER));
                return ps;
            }, keyHolder);
            return (Long) keyHolder.getKeys().get("id");
        }catch (Exception e) {
            throw new BankBadRequestException("invalid_details");
        }
    }

    @Override
    public User findByEmailAndPassword(String ssn, String password) throws BankAuthException {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_SSN, userRowMapper, ssn);
            if (!BCrypt.checkpw(password, user.getPassword()))
                throw new BankBadRequestException("invalid_credentials");
            return user;
        }catch (EmptyResultDataAccessException e){
            throw new BankBadRequestException("invalid_credentials");
        }
    }

    @Override
    public Integer getCountByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, Integer.class, email);
    }

    @Override
    public Integer getCountBySSN(String ssn) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_SSN, Integer.class, ssn);
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
            Timestamp lastModifiedDate = new Timestamp(time);
            user.setLastModifiedDate(lastModifiedDate);
            user.setLastModifiedBy(user.getFirstname() + " " + user.getLastname());
            jdbcTemplate.update(SQL_UPDATE, user.getSsn(), user.getFirstname(), user.getLastname(), user.getEmail(),
                    user.getAddress(), user.getMobilePhoneNumber(), user.getLastModifiedBy(),
                    user.getLastModifiedDate(), id);
        }catch (Exception e){
            throw new BankBadRequestException("invalid_request");
        }
    }

    @Override
    public void updatePassword(Long id, String new_password, String old_password) throws BankBadRequestException {
        checkPasswordEquity(old_password, id);
        try {
            String hashedPassword = BCrypt.hashpw(new_password, BCrypt.gensalt(10));
            jdbcTemplate.update(SQL_UPDATE_PASSWORD, hashedPassword,  id);
        }catch (Exception e){
            throw new BankBadRequestException("invalid_request");
        }
    }

    @Override
    public void removeById(Long id) throws BankResourceNotFoundException {
        int count = jdbcTemplate.update(SQL_DELETE, id);
        if (count == 0)
            throw new BankResourceNotFoundException("user_not_found");
    }

    public void checkPasswordEquity(String old_password, Long id){
        User user = jdbcTemplate.queryForObject(SQL_FIND_BY_ID, userRowMapper, id);
        if (!(BCrypt.hashpw(old_password, user.getPassword()).equals(user.getPassword())))
            throw new BankBadRequestException("password_does_not_match");

    }

    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
       return new User(rs.getLong("id"),
               rs.getString("ssn"),
               rs.getString("first_name"),
               rs.getString("last_name"),
               rs.getString("email"),
               rs.getString("password_hash"),
               rs.getString("address"),
               rs.getString("mobile_phone_number"),
               rs.getString("created_by"),
               rs.getTimestamp("created_date"),
               rs.getString("last_modified_by"),
               rs.getTimestamp("last_modified_date"),
               AppUserRole.USER);
    });
}
