package com.aziz.auth_service.dto;

import com.aziz.auth_service.util.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class AuthUserDto {
    private Long userId;
    private Role role;
}