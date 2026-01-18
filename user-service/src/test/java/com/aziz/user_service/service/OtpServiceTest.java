package com.aziz.user_service.service;

import com.aziz.user_service.dto.OtpVerificationRequest;
import com.aziz.user_service.repository.OtpRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static com.aziz.user_service.TestDataUtility.createOtpVerificationRequest;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @InjectMocks
    private OtpService service;

    @Mock
    private OtpRepository repository;

    private static final Duration OTP_DURATION = Duration.ofMinutes(5);
    private static final int OTP_LENGTH = 6;

    private OtpVerificationRequest otpVerificationRequest;

    @BeforeEach
    void setUp() {
        otpVerificationRequest = createOtpVerificationRequest();
    }

//    @Test
//    void createOtp_shouldCreateOtp() {
//        OtpVerificationRequest result = service.createOtp(otpVerificationRequest.getEmail());
//
//        assertNotNull(result);
//        assertNotNull(result.getVerificationId());
//        assertEquals(otpVerificationRequest.getEmail(), result.getEmail());
//        assertEquals(6, otpVerificationRequest.getOtp().length());
//
//        verify(repository, times(1)).save(eq(result), eq(Duration.ofMinutes(5)));
//    }

    @Test
    void verifyAndGetEmail_shouldReturnEmail() {
        when(repository);
    }

    @Test
    void verifyAndGetEmail_shouldThrowNotFoundException() {

    }

    @Test
    void verifyAndGetEmail_shouldThrowInvalidOtpException() {

    }

}