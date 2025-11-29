package com.aziz.api_gateway.jwt;

import com.aziz.api_gateway.config.SessionConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class SessionManagementFilter extends OncePerRequestFilter {
    private final SessionConfig config;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean authenticated = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);

        String sessionId = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (config.getCookie().equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                }
            }
        }

        // generate session id only for guest
        if (!authenticated && sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(config.getCookie(), sessionId);
            cookie.setMaxAge((int) config.getMaxAge());
            cookie.setSecure(config.isSecure());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            //cookie.setSameSite("Lax");
            response.addCookie(cookie);
        }

        String finalSessionId = sessionId;

        // Wrap request to make sessionid available in header if microservices need it
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request) {
            @Override
            public String getHeader(String name) {
                if (name.equalsIgnoreCase(config.getHeaderName()) && finalSessionId != null) {
                    return finalSessionId;
                }

                return super.getHeader(name);
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                Set<String> names = new HashSet<>(Collections.list(super.getHeaderNames()));
                if (finalSessionId != null) {
                    names.add(config.getHeaderName());
                }

                return Collections.enumeration(names);
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                if (name.equalsIgnoreCase(config.getHeaderName()) && finalSessionId != null) {
                    return Collections.enumeration(Collections.singleton(finalSessionId));
                }

                return super.getHeaders(name);
            }
        };

        filterChain.doFilter(wrapper, response);
    }
}