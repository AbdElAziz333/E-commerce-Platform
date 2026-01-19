package com.aziz.user_service.util.exceptions;

import com.aziz.user_service.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiExceptions(ApiException ex) {
        ApiError apiError = new ApiError(ex.getHttpStatus().value(), ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(apiError, ex.getHttpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error"));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodValidation(
            HandlerMethodValidationException ex
    ) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("Invalid request parameter"));
    }
}