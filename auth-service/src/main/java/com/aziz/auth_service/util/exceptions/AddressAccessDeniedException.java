package com.aziz.auth_service.util.exceptions;

import org.springframework.http.HttpStatus;

public class AddressAccessDeniedException extends ApiException {
    public AddressAccessDeniedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
