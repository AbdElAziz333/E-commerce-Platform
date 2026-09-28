package com.aziz.product.util.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ApiError {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}
