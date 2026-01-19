package com.aziz.auth_service.util.exceptions;

import org.springframework.http.HttpStatus;

public class BadCredentialsException extends ApiException {
    public BadCredentialsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}