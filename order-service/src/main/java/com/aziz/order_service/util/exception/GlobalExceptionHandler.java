package com.aziz.order_service.util.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiExceptions(ApiException ex) {
        ApiError apiError = new ApiError(ex.getHttpStatus().value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiError, ex.getHttpStatus());
    }
}