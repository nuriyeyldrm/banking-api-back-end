package com.banking.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BankConflictException extends RuntimeException{

    public BankConflictException(String message) {
        super(message);
    }
}
