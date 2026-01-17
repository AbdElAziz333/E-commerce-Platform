package com.aziz.order_service.util.exception;

import org.springframework.http.HttpStatus;

public class OrderAccessDeniedException extends ApiException {
    public OrderAccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}