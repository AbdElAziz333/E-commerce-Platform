package com.aziz.gateway.mapper;

import com.aziz.gateway.dto.request.UserCreationRequest;
import com.aziz.gateway.dto.response.UserDto;
import com.aziz.gateway.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName());
    }

    public User creationRequestToUser(UserCreationRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .build();
    }
}