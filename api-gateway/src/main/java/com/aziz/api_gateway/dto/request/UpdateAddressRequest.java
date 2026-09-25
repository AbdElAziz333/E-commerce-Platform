package com.aziz.api_gateway.dto.request;

import com.aziz.api_gateway.util.enums.City;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAddressRequest {
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