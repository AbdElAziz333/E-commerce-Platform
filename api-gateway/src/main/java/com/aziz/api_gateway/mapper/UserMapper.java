package com.aziz.api_gateway.mapper;

import com.aziz.api_gateway.dto.response.UserDto;
import com.aziz.api_gateway.model.RegistrationSession;
import com.aziz.api_gateway.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName());
    }

    public User registrationSessionToUser(RegistrationSession session) {
        return User.builder()
                .firstName(session.getFirstName())
                .lastName(session.getLastName())
                .email(session.getEmail())
                .password(session.getPassword())
                .phoneNumber(session.getPhoneNumber())
                .build();
    }
}