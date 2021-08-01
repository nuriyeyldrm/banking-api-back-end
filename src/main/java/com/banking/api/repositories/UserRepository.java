package com.banking.api.repositories;

import com.banking.api.domain.User;
import com.banking.api.exceptions.BankAuthException;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;

import java.sql.Timestamp;
import java.util.List;

public interface UserRepository {

    Long create(String ssn, String firstName, String lastName, String email, String password, String address,
                String mobilePhoneNumber, String createdBy, Timestamp createdDate, String lastModifiedBy,
                Timestamp lastModifiedDate) throws BankBadRequestException;

    User findByEmailAndPassword(String ssn, String password) throws BankAuthException;

    // check email is already in use or not
    Integer getCountByEmail(String email);

    // check email is already in use or not
    Integer getCountBySSN(String ssn);

    User findById(Long id) throws BankResourceNotFoundException;

    List<User> findAll() throws BankResourceNotFoundException;

    void update(Long id, User user) throws BankBadRequestException;

    void updatePassword(Long id, String new_password, String old_password) throws BankBadRequestException;

    void removeById(Long id)  throws BankResourceNotFoundException;

}
