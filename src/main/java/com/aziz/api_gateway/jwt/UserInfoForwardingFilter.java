package com.aziz.api_gateway.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
public class UserInfoForwardingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            Long userId = (Long) auth.getPrincipal();
            String role = auth.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("");

            // Wrap the request to add custom headers
            HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request) {
                private final Map<String, String> customHeaders = new HashMap<>();

                {
                    customHeaders.put("x-user-id", userId.toString());
                    customHeaders.put("x-user-role", role);
                }

                @Override
                public String getHeader(String name) {
                    String headerValue = customHeaders.get(name.toLowerCase());

                    if (headerValue != null) {
                        return headerValue;
                    }

                    return super.getHeader(name);
                }

                @Override
                public Enumeration<String> getHeaderNames() {
                    Set<String> names = new HashSet<>(customHeaders.keySet());
                    Enumeration<String> originalNames = super.getHeaderNames();

                    while (originalNames.hasMoreElements()) {
                        names.add(originalNames.nextElement());
                    }

                    return Collections.enumeration(names);
                }

                @Override
                public Enumeration<String> getHeaders(String name) {
                    String headerValue = customHeaders.get(name.toLowerCase());

                    if (headerValue != null) {
                        return Collections.enumeration(Collections.singletonList(headerValue));
                    }

                    return super.getHeaders(name);
                }
            };

            filterChain.doFilter(wrappedRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}