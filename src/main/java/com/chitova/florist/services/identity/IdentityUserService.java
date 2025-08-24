package com.chitova.florist.services.identity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

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
                .firstname(jwt.getClaimAsString("given_name"))
                .lastname(jwt.getClaimAsString("family_name"))
                .emailVerified(jwt.getClaimAsBoolean("email_verified"))
                .build();
    }
}
