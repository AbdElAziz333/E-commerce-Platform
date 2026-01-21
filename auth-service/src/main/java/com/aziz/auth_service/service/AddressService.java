package com.aziz.auth_service.service;

import com.aziz.auth_service.dto.AddressDto;
import com.aziz.auth_service.mapper.AddressMapper;
import com.aziz.auth_service.model.Address;
import com.aziz.auth_service.model.User;
import com.aziz.auth_service.repository.AddressRepository;
import com.aziz.auth_service.request.CreateAddressRequest;
import com.aziz.auth_service.request.UpdateAddressRequest;
import com.aziz.auth_service.util.exceptions.AddressAccessDeniedException;
import com.aziz.auth_service.util.exceptions.NotFoundException;
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
        Address address = mapper.registerRequestToAddress(request);
        address.setUser(user);

        repository.save(address);
        log.info("Address {} created successfully for user {}", address.getId(), userId);
        return mapper.addressToDto(address);
    }

    @Transactional
    public AddressDto updateAddress(Long userId, UpdateAddressRequest request) {
        Address address = getAddressForUser(userId, request.getId());

        address.setStreetLine1(request.getStreetLine1());
        address.setStreetLine2(request.getStreetLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setIsDefaultShipping(request.getIsDefaultShipping());

        log.info("Address with id: {} successfully updated", request.getId());
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
            throw new AddressAccessDeniedException("Access denied for address: " + addressId);
        }

        return address;
    }
}