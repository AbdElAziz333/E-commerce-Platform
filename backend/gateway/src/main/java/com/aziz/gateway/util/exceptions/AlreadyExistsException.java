package com.aziz.gateway.util.exceptions;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends ApiException {
    public AlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}