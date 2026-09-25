package com.aziz.api_gateway.dto.response;

import com.aziz.api_gateway.util.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthUserDto {
    private Long userId;
    private Role role;
}