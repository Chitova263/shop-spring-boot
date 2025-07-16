package com.chitova.florist.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdentityUserService {

    public IdentityUser getIdentityUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        Jwt jwt = jwtAuthenticationToken.getToken();
        return IdentityUser.builder()
                .sub(jwt.getSubject())
                .email(jwt.getClaimAsString("email"))
                .firstname(Optional.ofNullable(jwt.getClaimAsString("given_name")).orElse(null))
                .lastname(Optional.ofNullable(jwt.getClaimAsString("family_name")).orElse(null))
                .emailVerified(jwt.getClaimAsBoolean("email_verified"))
                .build();
    }
}
