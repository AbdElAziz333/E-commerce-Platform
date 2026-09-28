package com.aziz.order.util.exception;

import org.springframework.http.HttpStatus;

public class OrderAccessDeniedException extends ApiException {
    public OrderAccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}