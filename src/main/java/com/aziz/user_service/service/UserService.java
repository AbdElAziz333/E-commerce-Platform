package com.aziz.user_service.service;

import com.aziz.user_service.dto.*;
import com.aziz.user_service.model.User;
import com.aziz.user_service.repository.UserRepository;
import com.aziz.user_service.util.enums.PreferredLanguage;
import com.aziz.user_service.util.enums.Role;
import com.aziz.user_service.util.exceptions.*;
import com.aziz.user_service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return repository.findAll().stream().map(mapper::userToDto).toList();
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        return repository.findById(id)
                .map(mapper::userToDto)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    @Transactional
    public AuthUserDto createUser(PendingUserData data) {
        if (repository.existsByEmail(data.getEmail())) {
            throw new AlreadyExistsException("User already exists with email: " + data.getEmail());
        }

        User user = mapper.pendingUserDataToUser(data);
        user.setRole(Role.ROLE_USER);
        user.setPreferredLanguage(PreferredLanguage.ARABIC);

        repository.save(user);
        return new AuthUserDto(user.getUserId(), user.getRole());
    }

    @Transactional
    public UserDto updateUser(UserUpdateRequest request) {
        User user = repository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + request.getId()));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(encoder.encode(request.getPassword()));

        repository.save(user);
        return mapper.userToDto(user);
    }

    @Transactional
    public void deleteUserById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        repository.delete(user);
    }

    public User getUserEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }
}