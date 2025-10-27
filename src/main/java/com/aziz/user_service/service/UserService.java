package com.aziz.user_service.service;

import com.aziz.user_service.dto.PendingUserDto;
import com.aziz.user_service.dto.UserDto;
import com.aziz.user_service.dto.UserUpdateRequest;
import com.aziz.user_service.model.User;
import com.aziz.user_service.repository.UserRepository;
import com.aziz.user_service.util.PreferredLanguage;
import com.aziz.user_service.util.Role;
import com.aziz.user_service.util.exceptions.*;
import com.aziz.user_service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        log.debug("Fetching all users from database");
        List<UserDto> users = repository.findAll().stream().map(mapper::userToDto).toList();
        log.debug("Fetched {} users", users.size());
        return users;
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        log.debug("Fetching user with id: {}", id);

        return repository.findById(id)
                .map(user -> {
                    log.info("Successfully fetched user with id: {}", id);
                    return mapper.userToDto(user);
                })
                .orElseThrow(() -> {
                    log.warn("Cannot fetch user with id: {}, user not found", id);
                    return new NotFoundException("User not found with id: " + id);
                });
    }

    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        log.debug("Fetching user with email: {}", email);

        return repository.findByEmail(email)
                .map(user -> {
                    log.info("Successfully fetched user with email: {}", email);
                    return mapper.userToDto(user);
                })
                .orElseThrow(() -> {
                    log.warn("Cannot fetch user with email {}, user not found", email);
                    return new NotFoundException("User not found with email: " + email);
                });
    }

    public void createUser(PendingUserDto dto) {
        log.debug("Attempting to register new user with email: {}", dto.getEmail());

        if (repository.existsByEmail(dto.getEmail())) {
            log.warn("User already exists with email: {}", dto.getEmail());
            throw new AlreadyExistsException("User already exists with email: " + dto.getEmail());
        }

        User user = mapper.pendingUserDtoToUser(dto);
        user.setRole(Role.ROLE_USER);
        user.setPreferredLanguage(PreferredLanguage.ARABIC);

        repository.save(user);
        log.info("User successfully created with id: {}", user.getUserId());
    }

    @Transactional
    public UserDto updateUser(UserUpdateRequest updateRequest) {
        log.debug("Attempting to update user with id: {}", updateRequest.getId());

        User user = repository.findById(updateRequest.getId()).orElseThrow(
                () -> {
                    log.warn("Cannot update user -- user not found with id: {}", updateRequest.getId());
                    return new NotFoundException("User not found with id: " + updateRequest.getId());
                });

        user.setFirstName(updateRequest.getFirstName());
        user.setLastName(updateRequest.getLastName());
        user.setPassword(encoder.encode(updateRequest.getPassword()));
        user.setPhoneNumber(updateRequest.getPhoneNumber());
        repository.save(user);
        log.info("Successfully updated user with id: {}", updateRequest.getId());
        return mapper.userToDto(user);
    }

    @Transactional
    public void deleteUserById(Long id) {
        log.debug("Attempting to delete user with id: {}", id);

        User user = repository.findById(id).orElseThrow(
                () -> {
                    log.warn("Cannot delete user -- user not found with id: {}", id);
                    return new NotFoundException("User not found with id: " + id);
                });

        repository.delete(user);
        log.info("Successfully deleted user with id: {}", id);
    }

    @Transactional
    public void deleteUserByEmail(String email) {
        log.debug("Attempting to delete user with email: {}", email);

        User user = repository.findByEmail(email).orElseThrow(
                () -> {
                    log.warn("Cannot delete user -- user not found with email: {}", email);
                    return new NotFoundException("User not found with email: " + email);
                });

        repository.delete(user);
        log.info("Successfully deleted user with email: {}", email);
    }

    public boolean userExistsById(Long id) {
        return repository.existsById(id);
    }
}