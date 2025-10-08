package com.aziz.user_service.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UserRegisterRequest {
    @NotEmpty
    @NotBlank
    @Size(max = 30, message = "Firstname length must be less than 30 characters.")
    private String firstName;

    @NotEmpty
    @NotBlank
    @Size(max = 30, message = "Lastname length must be less than 30 characters.")
    private String lastName;

    @NotEmpty
    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty
    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters.")
    private String password;

    @NotEmpty
    @NotBlank
    @Length(min = 11, max = 13, message = "Phone number must be between 11 and 13 digits.")
    private String phoneNumber;
}