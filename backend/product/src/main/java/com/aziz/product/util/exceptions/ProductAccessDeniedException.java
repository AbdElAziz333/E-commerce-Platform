package com.aziz.product.util.exceptions;

import org.springframework.http.HttpStatus;

public class ProductAccessDeniedException extends ApiException {
    public ProductAccessDeniedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}