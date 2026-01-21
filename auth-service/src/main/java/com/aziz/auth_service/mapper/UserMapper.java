package com.aziz.auth_service.mapper;

import com.aziz.auth_service.dto.UserDto;
import com.aziz.auth_service.model.RegistrationSession;
import com.aziz.auth_service.model.User;
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