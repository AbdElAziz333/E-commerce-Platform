package com.aziz.auth_service.service;

import com.aziz.auth_service.dto.AddressDto;
import com.aziz.auth_service.mapper.AddressMapper;
import com.aziz.auth_service.model.Address;
import com.aziz.auth_service.model.User;
import com.aziz.auth_service.repository.AddressRepository;
import com.aziz.auth_service.request.AddressRegisterRequest;
import com.aziz.auth_service.request.AddressUpdateRequest;
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
    public Page<AddressDto> getPaginatedAddresses(Long userId, int page) {
        log.debug("Fetching addresses page {} for user: {}", page, userId);

        Pageable pageable = PageRequest.of(page, 100, Sort.by("createdAt").ascending());
        Page<AddressDto> addresses = repository.findAllByUserId(userId, pageable).map(mapper::addressToDto);

        log.info("Fetched {} addresses for user: {}", addresses.getNumberOfElements(), userId);
        return addresses;
    }

    @Transactional(readOnly = true)
    public AddressDto getAddressById(Long userId, Long id) {
        log.debug("Fetching address with id: {}", id);

        return repository.findByIdAndUserId(id, userId)
                .map(address -> {
                    log.info("Successfully fetched address with id: {}", id);
                    return mapper.addressToDto(address);
                })
                .orElseThrow(() -> {
                    log.warn("Cannot fetch address with id: {}, address not found", id);
                    return new NotFoundException("Address not found with id: " + id);
                });
    }

    @Transactional
    public AddressDto addAddress(Long userId, AddressRegisterRequest request) {
        log.debug("Attempting to add a new address with user id: {}", userId);

        User user = userService.getUserEntityById(userId);
        Address address = mapper.registerRequestToAddress(request);
        address.setUser(user);

        repository.save(address);
        log.info("Address successfully created with id: {}", address.getId());
        return mapper.addressToDto(address);
    }

    @Transactional
    public AddressDto updateAddress(Long userId, AddressUpdateRequest request) {
        log.debug("Attempting to update address with id: {}", request.getId());

        Address address = repository.findByIdAndUserId(request.getId(), userId)
                .orElseThrow(() -> {
                    log.warn("Cannot update address with id: {}, address not found", request.getId());
                    return new NotFoundException("Address not found with id: " + request.getId());
                });

        address.setStreetLine1(request.getStreetLine1());
        address.setStreetLine2(request.getStreetLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setIsDefaultShipping(request.getIsDefaultShipping());

//        repository.save(address);

        log.info("Address with id: {} successfully updated", request.getId());
        return mapper.addressToDto(address);
    }

    @Transactional
    public void deleteAddress(Long userId, Long id) {
        log.debug("Attempting to delete address with id: {}", id);

        Address address = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> {
                    log.warn("Cannot delete address with id: {}, address not found", id);
                    return new NotFoundException("Address not found with id: " + id);
                });

        repository.delete(address);
        log.info("Address with id: {} successfully deleted", id);
    }
}