package com.aziz.user_service.mapper;

import com.aziz.user_service.dto.AddressDto;
import com.aziz.user_service.request.AddressRegisterRequest;
import com.aziz.user_service.model.Address;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressMapper {
    public AddressDto addressToDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .streetLine1(address.getStreetLine1())
                .streetLine2(address.getStreetLine2())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .isDefaultShipping(address.getIsDefaultShipping())
                .build();
    }

    public List<AddressDto> addressesToDtos(List<Address> addresses) {
        return addresses.stream()
                .map(this::addressToDto)
                .toList();
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
}