package com.aziz.user_service.mappers;

import com.aziz.user_service.dto.AddressDto;
import com.aziz.user_service.dto.AddressRegisterRequest;
import com.aziz.user_service.model.Address;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AddressMapper {
    public AddressDto addressToDto(Address address) {
        return AddressDto.builder()
                .addressId(address.getAddressId())
                .streetLine1(address.getStreetLine1())
                .streetLine2(address.getStreetLine2())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .isDefaultShipping(address.getIsDefaultShipping())
                .build();
    }

    public List<AddressDto> addressesToDtos(List<Address> addresses) {
        List<AddressDto> addressDtos = new ArrayList<>();

        for (Address address : addresses) {
            addressDtos.add(
                    AddressDto.builder()
                            .addressId(address.getAddressId())
                            .streetLine1(address.getStreetLine1())
                            .streetLine2(address.getStreetLine2())
                            .city(address.getCity())
                            .state(address.getState())
                            .postalCode(address.getPostalCode())
                            .isDefaultShipping(address.getIsDefaultShipping())
                            .build()
            );
        }

        return addressDtos;
    }

    public Address registerRequestToAddress(AddressRegisterRequest request) {
        return Address.builder()
                .streetLine1(request.getStreetLine1())
                .streetLine2(request.getStreetLine2())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .isDefaultShipping(request.getIsDefaultShipping())
                .build();
    }

    public Address dtoToAddress(AddressDto dto) {
        return Address.builder()
                .addressId(dto.getAddressId())
                .streetLine1(dto.getStreetLine1())
                .streetLine2(dto.getStreetLine2())
                .city(dto.getCity())
                .state(dto.getState())
                .postalCode(dto.getPostalCode())
                .isDefaultShipping(dto.getIsDefaultShipping())
                .build();
    }
}