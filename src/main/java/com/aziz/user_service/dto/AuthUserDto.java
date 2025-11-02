package com.aziz.user_service.dto;

import com.aziz.user_service.util.enums.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDto {
    private Long userId;
    private Role role;
}