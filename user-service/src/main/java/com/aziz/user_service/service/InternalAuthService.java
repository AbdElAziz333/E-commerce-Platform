package com.aziz.user_service.service;

import com.aziz.user_service.dto.AuthUserDto;
import com.aziz.user_service.dto.CurrentUserDto;
import com.aziz.user_service.dto.LoginRequest;
import com.aziz.user_service.mapper.AddressMapper;
import com.aziz.user_service.model.User;
import com.aziz.user_service.repository.UserRepository;
import com.aziz.user_service.util.exceptions.BadCredentialsException;
import com.aziz.user_service.util.exceptions.NotFoundException;
import com.aziz.user_service.util.exceptions.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalAuthService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final AddressMapper addressMapper;

    @Transactional
    public AuthUserDto verifyCredentials(LoginRequest request) {
        log.debug("Attempting to verify credentials for a user with email: {}, so he can login", request.getEmail());

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("Cannot verify credentials for user with email: {}, user not found", request.getEmail());
                    return new UsernameNotFoundException("User not found");
                });

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            log.error("Password are wrong, please enter a valid one.");
            throw new BadCredentialsException("Invalid credentials");
        }

        log.info("User with email: {}, successfully verified credentials and logged in", request.getEmail());
        return new AuthUserDto(user.getUserId(), user.getRole());
    }

    @Transactional(readOnly = true)
    public CurrentUserDto getCurrentUser(Long userId) {
        Assert.notNull(userId, "User email must not be null.");

        User user = repository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found with id: " + userId));

        return CurrentUserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .preferredLanguage(user.getPreferredLanguage())
                .addresses(addressMapper.addressesToDtos(user.getAddress()))
                .build();
    }

    @Transactional(readOnly = true)
    public String getCurrentUserEmail(Long userId) {
        return repository.findEmailById(userId).orElseThrow(() -> new NotFoundException("Current user's email not found!"));

    }
}