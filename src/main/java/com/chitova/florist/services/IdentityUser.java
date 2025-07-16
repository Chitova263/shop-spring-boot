package com.chitova.florist.services;

import lombok.Builder;

@Builder
public record IdentityUser(String sub,
                           String email,
                           String firstname,
                           String lastname,
                           boolean emailVerified) {
}
