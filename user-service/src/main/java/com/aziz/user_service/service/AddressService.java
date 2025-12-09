package com.aziz.user_service.service;

import com.aziz.user_service.dto.AddressDto;
import com.aziz.user_service.dto.AddressRegisterRequest;
import com.aziz.user_service.model.Address;
import com.aziz.user_service.model.User;
import com.aziz.user_service.repository.AddressRepository;
import com.aziz.user_service.util.exceptions.NotFoundException;
import com.aziz.user_service.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository repository;
    private final AddressMapper mapper;

    private final UserService userService;

    @Transactional(readOnly = true)
    public List<AddressDto> getAllAddresses() {
        log.debug("Fetching all addresses");
        List<AddressDto> addresses = repository.findAll().stream().map(mapper::addressToDto).toList();
        log.info("Fetched {} addresses", addresses.size());
        return addresses;
    }

    @Transactional(readOnly = true)
    public AddressDto getAddressById(Long id) {
        log.debug("Fetching address with id: {}", id);

        return repository.findById(id)
                .map(address -> {
                    log.info("Successfully fetched address with id: {}", id);
                    return mapper.addressToDto(address);
                })
                .orElseThrow(() -> {
                    log.error("Cannot fetch address with id: {}, address not found", id);
                    return new NotFoundException("Address not found with id: " + id);
                });
    }

    @Transactional(readOnly = true)
    public List<AddressDto> getAllAddressesByUserId(Long userId) {
        log.debug("Fetching all addresses for user with id: {}", userId);
        List<AddressDto> addresses = repository.findAllByUser_UserId(userId).stream().map(mapper::addressToDto).toList();
        log.info("Fetched {} address for user with id: {}", addresses.size(), userId);
        return addresses;
    }

    @Transactional
    public AddressDto addAddress(AddressRegisterRequest request, Long userId) {
        log.debug("Attempting to add a new address with user id: {}", userId);

        User user = userService.getUserEntityById(userId);
        Address address = mapper.registerRequestToAddress(request);
        address.setUser(user);

        repository.save(address);
        log.info("Address successfully created with id: {}", address.getAddressId());
        return mapper.addressToDto(address);
    }

    //TODO: needs rewriting
    @Transactional
    public AddressDto updateAddress(AddressDto dto) {
        log.debug("Attempting to update address with id: {}", dto.getAddressId());

        Address address = repository.findById(dto.getAddressId())
                .orElseThrow(() -> {
                    log.error("Cannot update address with id: {}, address not found", dto.getAddressId());
                    return new NotFoundException("Address not found with id: " + dto.getAddressId());
                });

        repository.save(mapper.dtoToAddress(dto));
        log.info("Address with id: {} successfully updated", dto.getAddressId());
        return dto;
    }

    @Transactional
    public void deleteAddress(Long id) {
        log.debug("Attempting to delete address with id: {}", id);

        Address address = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot delete address with id: {}, address not found", id);
                    return new NotFoundException("Address not found with id: " + id);
                });

        repository.delete(address);
        log.info("Address with id: {} successfully deleted", id);
    }
}