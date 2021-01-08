package com.paymybuddy.webapp.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class FunctionalException extends RuntimeException{
    public FunctionalException(String message)
    {
        super(message);
    }
}
