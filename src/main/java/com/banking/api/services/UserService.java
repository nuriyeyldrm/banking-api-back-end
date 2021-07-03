package com.banking.api.services;

import com.banking.api.domain.User;
import com.banking.api.exceptions.BankAuthException;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;

import java.sql.Timestamp;
import java.util.List;

public interface UserService {

    User validateUser(String email, String password) throws BankAuthException;

    User registerUser(String firstname, String lastname, String email, String password, String createdBy,
                      Timestamp createdDate, String lastModifiedBy, Timestamp lastModifiedDate) throws BankAuthException;

    List<User> fetchAllUsers();

    User fetchUserById(Long id) throws BankResourceNotFoundException;

    void updateUser(Long id, User user) throws BankBadRequestException;

    void removeUser(Long id) throws BankResourceNotFoundException;
}
