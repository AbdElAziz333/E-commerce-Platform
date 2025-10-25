package com.aziz.user_service.mapper;

import com.aziz.user_service.dto.UserDto;
import com.aziz.user_service.model.PendingUser;
import com.aziz.user_service.model.User;
import com.aziz.user_service.util.PreferredLanguage;
import com.aziz.user_service.util.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto userToDto(User user) {
        return new UserDto(user.getUserId(), user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getRole());
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