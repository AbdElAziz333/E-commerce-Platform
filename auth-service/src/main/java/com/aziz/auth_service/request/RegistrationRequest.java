package com.aziz.auth_service.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    @NotBlank
    @Size(max = 30, message = "Firstname length must be less than 30 characters.")
    private String firstName;

    @NotBlank
    @Size(max = 30, message = "Lastname length must be less than 30 characters.")
    private String lastName;

    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters.")
    private String password;

    @NotBlank
    @Length(min = 11, max = 13, message = "Phone number must be between 11 and 13 digits.")
    private String phoneNumber;
}