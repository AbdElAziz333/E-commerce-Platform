package com.aziz.auth_service.service;

import com.aziz.auth_service.dto.AuthUserDto;
import com.aziz.auth_service.dto.CurrentUserDto;
import com.aziz.auth_service.dto.PendingUserData;
import com.aziz.auth_service.dto.UserDto;
import com.aziz.auth_service.mapper.AddressMapper;
import com.aziz.auth_service.mapper.UserMapper;
import com.aziz.auth_service.model.User;
import com.aziz.auth_service.repository.UserRepository;
import com.aziz.auth_service.request.UserUpdateRequest;
import com.aziz.auth_service.util.enums.Role;
import com.aziz.auth_service.util.enums.PreferredLanguage;
import com.aziz.auth_service.util.exceptions.AlreadyExistsException;
import com.aziz.auth_service.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final AddressMapper addressMapper;

    @Transactional(readOnly = true)
    public Page<UserDto> getUsers(int page) {
        log.debug("Fetching users for page: {}", page);

        Pageable pageable = PageRequest.of(page, 100, Sort.by("createdAt").ascending());
        Page<UserDto> users = repository.findAll(pageable).map(mapper::userToDto);

        log.info("Fetched {} users for page: {}", users.getNumberOfElements(), page);
        return users;
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        log.debug("Fetching user with id: {}", id);

        return repository.findById(id)
                .map((user) -> {
                    log.info("Successfully fetched user with id: {}", id);
                    return mapper.userToDto(user);
                })
                .orElseThrow(() -> {
                    log.warn("Cannot fetch user with id: {}, user not found", id);
                    return new NotFoundException("User not found with id: " + id);
                });
    }

    @Transactional
    public AuthUserDto createUser(PendingUserData data) {
        log.debug("Attempting to create a new user with email: {}", data.getEmail());

        if (repository.existsByEmail(data.getEmail())) {
            log.warn("Cannot create user with email: {}, user already exists", data.getEmail());
            throw new AlreadyExistsException("User already exists with email: " + data.getEmail());
        }

        User user = mapper.pendingUserDataToUser(data);
        user.setRole(Role.ROLE_USER);
        user.setPreferredLanguage(PreferredLanguage.ARABIC);

        repository.save(user);
        log.info("User successfully created with id: {}", user.getId());
        return new AuthUserDto(user.getId(), user.getRole());
    }

    @Transactional
    public UserDto updateUser(Long userId, UserUpdateRequest request) {
        log.debug("Attempting to update user with id: {}", userId);

        User user = repository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Cannot update user info -- user not found with id: {}", userId);
                    return new NotFoundException("User not found with id: " + userId);
                });

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(encoder.encode(request.getPassword()));
        }

        log.info("User updated successfully with id: {}", userId);
        return mapper.userToDto(user);
    }

    @Transactional
    public void deleteUserById(Long id) {
        log.debug("Attempting to delete user with id: {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cannot delete user with id: {}, user not found", id);
                    return new NotFoundException("User not found with id: " + id);
                });

        repository.delete(user);
        log.info("User with id: {} successfully deleted", id);
    }

    @Transactional(readOnly = true)
    public User getUserEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public CurrentUserDto getCurrentUser(Long userId) {
        User user = repository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found with id: " + userId));

        return CurrentUserDto.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .preferredLanguage(user.getPreferredLanguage())
                .addresses(addressMapper.addressesToDtos(user.getAddresses()))
                .role(user.getRole())
                .build();
    }

    @Transactional(readOnly = true)
    public String getCurrentUserEmail(Long userId) {
        return repository.findEmailById(userId).orElseThrow(() -> new NotFoundException("Current user's email not found!"));
    }
}