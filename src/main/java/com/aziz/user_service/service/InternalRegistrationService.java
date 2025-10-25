package com.aziz.user_service.service;

import com.aziz.user_service.dto.*;
import com.aziz.user_service.mapper.PendingUserMapper;
import com.aziz.user_service.model.PendingUser;
import com.aziz.user_service.repository.PendingUserRepository;
import com.aziz.user_service.util.MailMessageSender;
import com.aziz.user_service.util.exceptions.AlreadyExistsException;
import com.aziz.user_service.util.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InternalRegistrationService {
    private final PendingUserRepository repository;
    private final PendingUserMapper mapper;
    private final OtpService otpService;
    private final MailMessageSender mailMessageSender;

    @Transactional
    public String createUser(RegistrationRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Pending user already exists with email: " + request.getEmail());
        }

        PendingUser user = mapper.registerRequestToPendingUser(request);

        repository.save(user);
        return otpService.sendOtpToEmail(request.getEmail());
    }

    @Transactional
    public PendingUserDto verifyOtp(OtpRequest request) {
        String email = otpService.verifyOtp(request.getVerificationId(), request.getOtp());

        PendingUserDto dto = repository.findByEmail(email)
                .map(mapper::pendingUserToDto)
                .orElseThrow(() -> new NotFoundException("Pending user not found"));

        mailMessageSender.sendEmailVerified(email, dto.getFirstName());

        return dto;
    }
}