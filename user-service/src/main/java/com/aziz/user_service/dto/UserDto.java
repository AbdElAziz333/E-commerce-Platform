package com.aziz.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDto {
    private Long userId;

    @NotEmpty
    @NotBlank
    @Size(max = 30, message = "Firstname length must be less than 30 characters.")
    private String firstName;

    @NotEmpty
    @NotBlank
    @Size(max = 30, message = "Lastname length must be less than 30 characters.")
    private String lastName;
}