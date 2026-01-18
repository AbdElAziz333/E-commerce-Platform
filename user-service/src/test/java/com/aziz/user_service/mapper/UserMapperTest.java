package com.aziz.user_service.mapper;

import com.aziz.user_service.dto.PendingUserData;
import com.aziz.user_service.dto.UserDto;
import com.aziz.user_service.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.aziz.user_service.TestDataUtility.createPendingUserData;
import static com.aziz.user_service.TestDataUtility.createUser;
import static org.junit.jupiter.api.Assertions.*;

//class UserMapperTest {
//    private static UserMapper mapper;
//
//    @BeforeAll
//    public static void setUp() {
//        mapper = new UserMapper();
//    }
//
//    @Test
//    void userToDtoTest() {
//        User user = createUser();
//        UserDto dto = mapper.userToDto(user);
//
//        assertEquals(user.getUserId(), dto.getUserId());
//        assertEquals(user.getFirstName(), dto.getFirstName());
//        assertEquals(user.getLastName(), dto.getLastName());
//    }
//
//    @Test
//    void pendingUserDataToUserTest() {
//        PendingUserData data = createPendingUserData();
//        User user = mapper.pendingUserDataToUser(data);
//
//        assertEquals(data.getFirstName(), user.getFirstName());
//        assertEquals(data.getLastName(), user.getLastName());
//        assertEquals(data.getEmail(), user.getEmail());
//        assertEquals(data.getPassword(), user.getPassword());
//        assertEquals(data.getPhoneNumber(), user.getPhoneNumber());
//        assertEquals(data.getLastName(), user.getLastName());
//    }
//}