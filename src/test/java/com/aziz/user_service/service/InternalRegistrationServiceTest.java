package com.aziz.user_service.service;

import com.aziz.user_service.dto.AuthUserDto;
import com.aziz.user_service.dto.OtpVerificationRequest;
import com.aziz.user_service.dto.PendingUserData;
import com.aziz.user_service.dto.RegistrationRequest;
import com.aziz.user_service.mapper.UserMapper;
import com.aziz.user_service.repository.PendingUserRepository;
import com.aziz.user_service.repository.UserRepository;
import com.aziz.user_service.util.MailMessageSender;
import com.aziz.user_service.util.exceptions.AlreadyExistsException;
import com.aziz.user_service.util.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.aziz.user_service.TestDataUtility.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InternalRegistrationServiceTest {
    @Mock
    private OtpService otpService;

    @Mock
    private MailMessageSender mailMessageSender;

    @Mock
    private UserService userService;

    @Mock
    private PendingUserRepository pendingUserRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private InternalRegistrationService service;

    private RegistrationRequest registrationRequest;
    private OtpVerificationRequest otpVerificationRequest;
    private PendingUserData pendingUserData;
    private AuthUserDto authUserDto;

    @BeforeEach
    void setUp() {
        registrationRequest = createRegistrationRequest();
        otpVerificationRequest = createOtpVerificationRequest();
        pendingUserData = createPendingUserData();
        authUserDto = createAuthUserDto();
    }

    @Test
    void createUser_shouldCreateUser() {
        String encodedPassword = "encodedPassword";

        when(repository.existsByEmail(registrationRequest.getEmail())).thenReturn(false);
        when(otpService.createOtp(registrationRequest.getEmail())).thenReturn(otpVerificationRequest);
        when(encoder.encode(registrationRequest.getPassword())).thenReturn(encodedPassword);
        when(mapper.registrationRequestToPendingUserData(registrationRequest, encodedPassword, otpVerificationRequest.getVerificationId())).thenReturn(pendingUserData);

        String result = service.createUser(registrationRequest);

        assertEquals(otpVerificationRequest.getVerificationId(), result);

        verify(pendingUserRepository, times(1)).save(pendingUserData);
        verify(mailMessageSender, times(1)).sendEmailVerification(registrationRequest.getEmail(), otpVerificationRequest.getOtp());
    }

    @Test
    void createUser_shouldThrowAlreadyExistsException() {
        when(repository.existsByEmail(registrationRequest.getEmail())).thenReturn(true);
        assertThrows(AlreadyExistsException.class, () -> service.createUser(registrationRequest));

        verifyNoInteractions(otpService);
    }

    @Test
    void verifyOtp_shouldVerifyOtp() {
        when(otpService.verifyAndGetEmail(otpVerificationRequest.getVerificationId(), otpVerificationRequest.getOtp())).thenReturn(registrationRequest.getEmail());
        when(pendingUserRepository.findById(otpVerificationRequest.getVerificationId())).thenReturn(Optional.of(pendingUserData));
        when(userService.createUser(pendingUserData)).thenReturn(authUserDto);

        AuthUserDto result = service.verifyOtp(otpVerificationRequest);

        assertEquals(authUserDto.getUserId(), result.getUserId());
        verify(pendingUserRepository, times(1)).delete(otpVerificationRequest.getVerificationId());
        verify(mailMessageSender, times(1)).sendWelcomeEmail(otpVerificationRequest.getEmail(), pendingUserData.getFirstName());
    }

    @Test
    void verifyOtp_shouldThrowNotFoundException() {
        when(otpService.verifyAndGetEmail(otpVerificationRequest.getVerificationId(), otpVerificationRequest.getOtp())).thenReturn(registrationRequest.getEmail());
        when(pendingUserRepository.findById(otpVerificationRequest.getVerificationId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.verifyOtp(otpVerificationRequest));
    }

    @Test
    void verifyOtp_shouldThrowIllegalStateException() {
        when(otpService.verifyAndGetEmail(otpVerificationRequest.getVerificationId(), otpVerificationRequest.getOtp())).thenReturn("otheremail@example.com");
        when(pendingUserRepository.findById(otpVerificationRequest.getVerificationId())).thenReturn(Optional.of(pendingUserData));
//        when(pendingUserData.getEmail().equals(otpVerificationRequest.getEmail())).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> service.verifyOtp(otpVerificationRequest));
    }
}