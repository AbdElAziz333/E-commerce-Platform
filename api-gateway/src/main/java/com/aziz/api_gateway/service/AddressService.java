package com.aziz.api_gateway.service;

import com.aziz.api_gateway.dto.response.AddressDto;
import com.aziz.api_gateway.mapper.AddressMapper;
import com.aziz.api_gateway.model.Address;
import com.aziz.api_gateway.model.User;
import com.aziz.api_gateway.repository.AddressRepository;
import com.aziz.api_gateway.dto.request.CreateAddressRequest;
import com.aziz.api_gateway.dto.request.UpdateAddressRequest;
import com.aziz.api_gateway.util.exceptions.AccessDeniedException;
import com.aziz.api_gateway.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository repository;
    private final AddressMapper mapper;

    private final UserService userService;

    @Transactional(readOnly = true)
    public Page<AddressDto> getAddresses(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 100, Sort.by("createdAt").ascending());
        return repository.findAllByUserId(userId, pageable).map(mapper::addressToDto);
    }

    @Transactional(readOnly = true)
    public AddressDto getAddressById(Long userId, Long addressId) {
        log.debug("Fetching address: {}", addressId);
        return mapper.addressToDto(getAddressForUser(userId, addressId));
    }

    @Transactional
    public AddressDto addAddress(Long userId, CreateAddressRequest request) {
        log.debug("Creating address for user: {}", userId);

        User user = userService.getUserEntityById(userId);
        Address address = mapper.createRequestToAddress(request);
        address.setUser(user);

        repository.save(address);
        log.info("Address {} created successfully for user {}", address.getId(), userId);
        return mapper.addressToDto(address);
    }

    @Transactional
    public AddressDto updateAddress(Long userId, Long addressId, UpdateAddressRequest request) {
        Address address = getAddressForUser(userId, addressId);

        address.setStreetLine1(request.getStreetLine1());
        address.setStreetLine2(request.getStreetLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setIsDefaultShipping(request.getIsDefaultShipping());

        log.info("Address: {} updated for user: {}", addressId, userId);
        return mapper.addressToDto(address);
    }

    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        repository.delete(getAddressForUser(userId, addressId));
        log.info("Address: {} deleted for user {}", addressId, userId);
    }

    private Address getAddressForUser(Long userId, Long addressId) {
        Address address = repository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new NotFoundException("Address not found" + addressId));

        if (!address.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Access denied for address: " + addressId);
        }

        return address;
    }
}