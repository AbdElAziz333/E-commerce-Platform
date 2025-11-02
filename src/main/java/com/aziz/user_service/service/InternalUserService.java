package com.aziz.user_service.service;

import com.aziz.user_service.dto.CurrentUserDto;
import com.aziz.user_service.mapper.AddressMapper;
import com.aziz.user_service.model.User;
import com.aziz.user_service.repository.UserRepository;
import com.aziz.user_service.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalUserService {
    private final UserRepository repository;
    private final AddressMapper addressMapper;

    @Transactional(readOnly = true)
    public CurrentUserDto getCurrentUser(Long userId) {
        Assert.notNull(userId, "User email must not be null.");

        User user = repository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found with id: " + userId));

        return CurrentUserDto.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .preferredLanguage(user.getPreferredLanguage())
                .addresses(addressMapper.addressesToDtos(user.getAddress()))
                .build();

    }
}
