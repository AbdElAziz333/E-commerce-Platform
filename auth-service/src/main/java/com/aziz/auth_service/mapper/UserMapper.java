package com.aziz.auth_service.mapper;

import com.aziz.auth_service.dto.PendingUserData;
import com.aziz.auth_service.dto.UserDto;
import com.aziz.auth_service.model.User;
import com.aziz.auth_service.request.RegistrationRequest;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName());
    }

    public User pendingUserDataToUser(PendingUserData data) {
        return User.builder()
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .email(data.getEmail())
                .password(data.getPassword())
                .phoneNumber(data.getPhoneNumber())
                .build();
    }

    public PendingUserData registrationRequestToPendingUserData(RegistrationRequest request, String encodedPassword, String verificationId) {
        return PendingUserData.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encodedPassword)
                .phoneNumber(request.getPhoneNumber())
                .verificationId(verificationId)
                .build();
    }
}