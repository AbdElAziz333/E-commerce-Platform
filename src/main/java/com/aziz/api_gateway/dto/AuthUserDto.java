package com.aziz.api_gateway.dto;

import com.aziz.api_gateway.util.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class AuthUserDto {
    private Long userId;
    private Role role;
}