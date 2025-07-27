package com.chitova.florist.model.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterAccountRequest {
    @Schema(description = "Account member email address")
    @NotNull
    private String email;

    @Schema(description = "Account member password")
    @NotNull
    private String password;

    @Schema(description = "Account member name")
    @NotNull
    private String name;

    @Schema(description = "Set to true to enable passwordless authentication using a one-time token")
    private boolean oneTimePasswordToken;
}
