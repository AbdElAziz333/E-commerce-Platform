package com.aziz.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
}