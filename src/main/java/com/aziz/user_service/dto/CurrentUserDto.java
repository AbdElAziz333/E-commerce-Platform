package com.aziz.user_service.dto;

import com.aziz.user_service.util.PreferredLanguage;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private PreferredLanguage preferredLanguage;
    private List<AddressDto> addresses;
}