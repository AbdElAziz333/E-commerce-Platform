package com.aziz.order_service.util.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    public NotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}