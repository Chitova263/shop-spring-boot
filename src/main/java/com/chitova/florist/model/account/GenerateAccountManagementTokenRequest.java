package com.chitova.florist.model.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenerateAccountManagementTokenRequest {

    @Schema(description = "Account member email address")
    @NotNull
    private String email;

    @Schema(description = "Account member password")
    private String password;

    @Schema(description = "Set to true to enable passwordless authentication using a one-time token. Do not set the password if true")
    private boolean oneTimePasswordToken;
}
