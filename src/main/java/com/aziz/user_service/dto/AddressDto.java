package com.aziz.user_service.dto;

import com.aziz.user_service.util.enums.City;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressDto {
    private Long addressId;

    @NotEmpty
    @NotBlank
    private String streetLine1;

    @NotBlank
    private String streetLine2;

    @NotNull
    private City city;

    @NotEmpty
    @NotBlank
    private String state;

    @NotEmpty
    @NotBlank
    private String postalCode;

    @NotNull
    private Boolean isDefaultShipping;
}