package com.aziz.api_gateway.util.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiException {
    public InvalidCredentialsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}