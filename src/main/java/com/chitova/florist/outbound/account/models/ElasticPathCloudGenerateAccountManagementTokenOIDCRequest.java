package com.chitova.florist.outbound.account.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ElasticPathCloudGenerateAccountManagementTokenOIDCRequest {
    private AuthTokenData data;

    @Builder
    @Data
    public static class AuthTokenData {

        private String type;

        @JsonProperty("authentication_mechanism")
        private String authenticationMechanism;

        @JsonProperty("oauth_authorization_code")
        private String oauthAuthorizationCode;

        @JsonProperty("oauth_redirect_uri")
        private String oauthRedirectUri;

        @JsonProperty("oauth_code_verifier")
        private String oauthCodeVerifier;
    }
}
