package com.banking.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class BankAuthException extends RuntimeException{

    public BankAuthException(String message){
        super(message);

    }
}
