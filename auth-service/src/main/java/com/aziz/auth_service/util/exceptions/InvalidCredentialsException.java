package com.aziz.auth_service.util.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiException {
    public InvalidCredentialsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
