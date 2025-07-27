package com.chitova.florist.model.account;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenerateAccountManagementTokenOidcRequest {
    @NotNull
    private String oauthAuthorizationCode;
    @NotNull
    private String oauthRedirectUri;
    @NotNull
    private String oauthCodeVerifier;
}


