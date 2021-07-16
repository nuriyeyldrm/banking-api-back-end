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
    public User validateUser(String ssn, String password) throws BankAuthException {

        return userRepository.findByEmailAndPassword(ssn, password);
    }

    @Override
    public User registerUser(String ssn, String firstName, String lastName, String email, String password,
                             String address, String mobilePhoneNumber, String createdBy, Timestamp createdDate,
                             String lastModifiedBy, Timestamp lastModifiedDate) throws BankAuthException {

        // Convert email to lowercase  sensitive
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if (email != null)
            email = email.toLowerCase();

        if (!pattern.matcher(email).matches())
            throw new BankAuthException("Invalid email format");

        Pattern pattern1 = Pattern.compile("^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$");

        if (!pattern1.matcher(ssn).matches())
            throw new BankAuthException("Invalid SSN format");

        Integer count = userRepository.getCountByEmail(email);
        if(count > 0)
            throw new BankAuthException("Email already in use");

        Integer count1 = userRepository.getCountBySSN(ssn);
        if(count1 > 0)
            throw new BankAuthException("SSN already in use");

        Long id = userRepository.create(ssn, firstName, lastName, email, password, address, mobilePhoneNumber,
                createdBy, createdDate, lastModifiedBy, lastModifiedDate);
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
    public void updatePassword(Long id, String password) throws BankBadRequestException {
        userRepository.updatePassword(id, password);
    }

    @Override
    public void removeUser(Long id) throws BankResourceNotFoundException {
        this.fetchUserById(id);
        userRepository.removeById(id);
    }
}
