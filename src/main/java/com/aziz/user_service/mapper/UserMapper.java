package com.aziz.user_service.mapper;

import com.aziz.user_service.dto.UserDto;
import com.aziz.user_service.dto.UserRegisterRequest;
import com.aziz.user_service.model.PendingUser;
import com.aziz.user_service.model.User;
import com.aziz.user_service.util.PreferredLanguage;
import com.aziz.user_service.util.Role;
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

    public User pendingUserToUser(PendingUser pendingUser) {
        return User.builder()
                .firstName(pendingUser.getFirstName())
                .lastName(pendingUser.getLastName())
                .email(pendingUser.getEmail())
                .password(pendingUser.getPassword())
                .phoneNumber(pendingUser.getPhoneNumber())
                .role(Role.ROLE_USER)
                .preferredLanguage(PreferredLanguage.ARABIC)
                .build();
    }
}