package com.aziz.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {
    private Long id;

    @NotEmpty
    @NotBlank
    @Size(max = 30, message = "Firstname length must be less than 30 characters.")
    private String firstName;

    @NotEmpty
    @NotBlank
    @Size(max = 30, message = "Lastname length must be less than 30 characters.")
    private String lastName;
}