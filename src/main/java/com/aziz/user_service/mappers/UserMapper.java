package com.aziz.user_service.mappers;

import com.aziz.user_service.dto.UserDto;
import com.aziz.user_service.dto.UserRegisterRequest;
import com.aziz.user_service.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto userToDto(User user) {
        return new UserDto(user.getUserId(), user.getFirstName(), user.getLastName(), user.getPhoneNumber());
    }

    public User registerRequestToUser(UserRegisterRequest registerRequest) {
        return User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .phoneNumber(registerRequest.getPhoneNumber())
                .build();
    }
}