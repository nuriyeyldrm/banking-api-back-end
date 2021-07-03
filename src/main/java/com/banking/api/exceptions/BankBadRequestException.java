package com.banking.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BankBadRequestException extends RuntimeException{

    public BankBadRequestException(String message){
        super(message);
    }
}
