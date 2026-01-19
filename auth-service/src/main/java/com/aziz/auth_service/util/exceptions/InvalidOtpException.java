package com.aziz.auth_service.util.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidOtpException extends ApiException {
    public InvalidOtpException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}