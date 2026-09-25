package com.aziz.api_gateway.util.exceptions;

import org.springframework.http.HttpStatus;

public class AddressAccessDeniedException extends ApiException {
    public AddressAccessDeniedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}