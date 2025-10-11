package com.aziz.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

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

    @NotEmpty
    @NotBlank
    @Length(min = 11, max = 13, message = "Phone number must be between 11 and 13 digits.")
    private String phoneNumber;
}