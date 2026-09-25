package com.aziz.api_gateway.dto.response;

import com.aziz.api_gateway.util.enums.PreferredLanguage;
import com.aziz.api_gateway.util.enums.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;
    private PreferredLanguage preferredLanguage;
    private List<AddressDto> addresses;
}