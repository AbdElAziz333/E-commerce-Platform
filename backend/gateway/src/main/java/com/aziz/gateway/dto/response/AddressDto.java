package com.aziz.gateway.dto.response;

import com.aziz.gateway.util.enums.City;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private String streetLine1;
    private String streetLine2;
    private City city;
    private String state;
    private String postalCode;
    private Boolean isDefaultShipping;
}