package com.banking.api.services.impl;

import com.banking.api.domain.User;
import com.banking.api.exceptions.BankAuthException;
import com.banking.api.exceptions.BankBadRequestException;
import com.banking.api.exceptions.BankResourceNotFoundException;
import com.banking.api.repositories.UserRepository;
import com.banking.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Pattern;

// Service added for automatic bean detection
@Service
// Transactional behaviour to all methods inside this class
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User validateUser(String email, String password) throws BankAuthException {

        if (email != null)
            email = email.toLowerCase();
        return userRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public User registerUser(String firstname, String lastname, String email, String password, String createdBy,
                             Timestamp createdDate, String lastModifiedBy, Timestamp lastModifiedDate)
            throws BankAuthException {

        // Convert email to lowercase  sensitive
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if (email != null)
            email = email.toLowerCase();

        if (!pattern.matcher(email).matches())
            throw new BankAuthException("Invalid email format");

        Integer count = userRepository.getCountByEmail(email);
        if(count > 0)
            throw new BankAuthException("Email already in use");

        Long id = userRepository.create(firstname, lastname, email, password, createdBy, createdDate,
                lastModifiedBy, lastModifiedDate);
        return userRepository.findById(id);
    }

    @Override
    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User fetchUserById(Long id) throws BankResourceNotFoundException {
        return userRepository.findById(id);
    }

    @Override
    public void updateUser(Long id, User user)
            throws BankBadRequestException {
        userRepository.update(id, user);
    }

    @Override
    public void removeUser(Long id) throws BankResourceNotFoundException {
        this.fetchUserById(id);
        userRepository.removeById(id);
    }
}
