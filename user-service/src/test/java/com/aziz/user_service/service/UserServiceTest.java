package com.aziz.user_service.service;

import com.aziz.user_service.dto.AuthUserDto;
import com.aziz.user_service.dto.PendingUserData;
import com.aziz.user_service.dto.UserDto;
import com.aziz.user_service.dto.UserUpdateRequest;
import com.aziz.user_service.mapper.UserMapper;
import com.aziz.user_service.model.User;
import com.aziz.user_service.repository.UserRepository;
import com.aziz.user_service.util.enums.Role;
import com.aziz.user_service.util.exceptions.AlreadyExistsException;
import com.aziz.user_service.util.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.aziz.user_service.TestDataUtility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//    @InjectMocks
//    private UserService service;
//
//    @Mock
//    private UserRepository repository;
//
//    @Mock
//    private PasswordEncoder encoder;
//
//    @Mock
//    private UserMapper mapper;
//
//    private User user;
//    private UserDto userDto;
//    private PendingUserData pendingUserData;
//    private UserUpdateRequest userUpdateRequest;
//
//    @BeforeEach
//    void setUp() {
//        user = createUser();
//        userDto = createUserDto();
//        pendingUserData = createPendingUserData();
//        userUpdateRequest = createUserUpdateRequest();
//    }

//    @Test
//    void getAllUsers_shouldReturnAllUsers() {
//        when(repository.findAll()).thenReturn(List.of(user));
//        when(mapper.userToDto(user)).thenReturn(userDto);
//
//        List<UserDto> userDtoList = service.getAllUsers();
//
//        assertEquals(1, userDtoList.size());
//        assertEquals(user.getFirstName(), userDtoList.get(0).getFirstName());
//    }
//
//    @Test
//    void getUserById_shouldReturnUser() {
//        Long userId = 1L;
//
//        when(repository.findById(userId)).thenReturn(Optional.of(user));
//        when(mapper.userToDto(user)).thenReturn(userDto);
//
//        UserDto dto = service.getUserById(userId);
//
//        assertNotNull(dto);
//        assertEquals(user.getUserId(), dto.getUserId());
//        assertEquals(user.getFirstName(), dto.getFirstName());
//        assertEquals(user.getLastName(), dto.getLastName());
//        verify(repository, times(1)).findById(userId);
//    }
//
//    @Test
//    void getUserById_shouldThrowNotFoundException() {
//        Long userId = 1L;
//
//        when(repository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> service.getUserById(userId));
//
//        verify(repository, times(1)).findById(userId);
//    }
//
//    @Test
//    void createUser_shouldCreateUser() {
//        when(repository.existsByEmail(pendingUserData.getEmail())).thenReturn(false);
//        when(mapper.pendingUserDataToUser(pendingUserData)).thenReturn(user);
//
//        AuthUserDto result = service.createUser(pendingUserData);
//
//        assertEquals(user.getUserId(), result.getUserId());
//        assertEquals(Role.ROLE_USER, result.getRole());
//        verify(repository, times(1)).save(user);
//    }
//
//    @Test
//    void createUser_shouldThrowAlreadyExistsException() {
//        when(repository.existsByEmail(pendingUserData.getEmail())).thenReturn(true);
//
//        assertThrows(AlreadyExistsException.class, () -> service.createUser(pendingUserData));
//    }

//    @Test
//    void updateUser_shouldUpdateUser() {
//        when(repository.findById(user.getUserId())).thenReturn(Optional.of(user));
//        when(encoder.encode(userUpdateRequest.getPassword())).thenReturn("encodedPassword");
//        when(mapper.userToDto(user)).thenReturn(userDto);
//
//        UserDto result = service.updateUser(userUpdateRequest);
//
//        assertEquals(userDto, result);
//        assertEquals(userUpdateRequest.getFirstName(), user.getFirstName());
//        assertEquals(userUpdateRequest.getLastName(), user.getLastName());
//        assertEquals(userUpdateRequest.getPhoneNumber(), user.getPhoneNumber());
//        assertEquals("encodedPassword", user.getPassword());
//
//        verify(repository, times(1)).save(user);
//    }
//
//    @Test
//    void updateUser_shouldThrowNotFoundException() {
//        when(repository.findById(user.getUserId())).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> service.updateUser(userUpdateRequest));
//    }

//    @Test
//    void deleteUserById_shouldDeleteUser() {
//        when(repository.findById(user.getUserId())).thenReturn(Optional.of(user));
//
//        service.deleteUserById(user.getUserId());
//
//        verify(repository, times(1)).delete(user);
//    }
//
//    @Test
//    void deleteUserById_shouldThrowNotFoundException() {
//        when(repository.findById(user.getUserId())).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> service.deleteUserById(user.getUserId()));
//
//        verify(repository, never()).delete(user);
//    }
//
//    @Test
//    void getUserEntityById_shouldReturnUser() {
//        when(repository.findById(user.getUserId())).thenReturn(Optional.of(user));
//
//        User result = service.getUserEntityById(user.getUserId());
//
//        assertEquals(user.getFirstName(), result.getFirstName());
//        assertEquals(user.getLastName(), result.getLastName());
//        assertEquals(user.getEmail(), result.getEmail());
//    }
//
//
//}