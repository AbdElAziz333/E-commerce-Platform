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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class InternalAuthService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final AddressMapper addressMapper;

    public AuthUserDto verifyCredentials(LoginRequest request) {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

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
}