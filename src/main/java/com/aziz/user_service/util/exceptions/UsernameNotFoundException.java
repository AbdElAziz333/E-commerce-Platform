package com.aziz.user_service.util.exceptions;

import org.springframework.http.HttpStatus;

public class UsernameNotFoundException extends ApiException {
    public UsernameNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}