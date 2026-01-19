package com.aziz.auth_service.request;

import com.aziz.auth_service.util.enums.City;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddressRequest {
    @NotBlank
    private String streetLine1;

    private String streetLine2;

    @NotNull
    private City city;

    @NotBlank
    private String state;

    @NotBlank
    private String postalCode;

    @NotNull
    private Boolean isDefaultShipping;
}