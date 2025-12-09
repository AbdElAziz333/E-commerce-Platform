package com.aziz.product_service.util.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiExceptions(ApiException ex) {
        ApiError error = new ApiError(ex.getHttpStatus().value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(error, ex.getHttpStatus());
    }
}