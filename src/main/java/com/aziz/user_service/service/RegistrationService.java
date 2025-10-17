package com.aziz.user_service.service;

import com.aziz.user_service.dto.OtpRequest;
import com.aziz.user_service.dto.RegistrationRequest;
import com.aziz.user_service.mappers.PendingUserMapper;
import com.aziz.user_service.mappers.UserMapper;
import com.aziz.user_service.model.PendingUser;
import com.aziz.user_service.model.User;
import com.aziz.user_service.repository.PendingUserRepository;
import com.aziz.user_service.repository.UserRepository;
import com.aziz.user_service.security.UserDetailsServiceImpl;
import com.aziz.user_service.util.MailMessageSender;
import com.aziz.user_service.util.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final PendingUserRepository pendingUserRepository;
    private final OtpService otpService;
    private final PendingUserMapper mapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final MailMessageSender mailMessageSender;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public String signup(RegistrationRequest request) {
        PendingUser pendingUser = mapper.registerRequestToPendingUser(request);
        pendingUserRepository.save(pendingUser);
        return otpService.sendOtpToEmail(request.getEmail());
    }

    public void verifyOtp(OtpRequest request, HttpServletResponse httpResponse) {
        String email = otpService.verifyOtp(request.getVerificationId(), request.getOtp());

        PendingUser pendingUser = pendingUserRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Pending User not found or already verified."));

        User user = userMapper.pendingUserToUser(pendingUser);
        userRepository.save(user);
        pendingUserRepository.delete(pendingUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateToken(authentication);
        jwtService.addJwtToCookieResponse(jwt, httpResponse);

        mailMessageSender.sendEmailVerified(user.getEmail(), user.getFirstName());
    }
}