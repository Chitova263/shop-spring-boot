package com.chitova.florist.model.configuration;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class GetConfigurationResponse {

    @Schema(description = "openid connect configuration")
    private Set<OpenIdConnectConfiguration> openIdConnect;

    @Schema(description = "base url")
    private String baseUrl;

    @Builder
    @Data
    public static class OpenIdConnectConfiguration {
        @Schema(description = "oidc issuer")
        private String issuer;

        @Schema(description = "oidc provider", examples = "Google, Facebook")
        private String provider;

        @Schema(description = "oidc provider redirect uri")
        private String redirectUri;

        @Schema(description = "oidc provider redirect client id")
        private String clientId;

        private String clientSecret;

        @Schema(description = "oidc provider redirect client id")
        private String authorizationUrl;

        @Schema(description = "oidc response type")
        private String responseType;

        @Schema(description = "oidc scopes")
        private String scope;

        @Schema(description = "oidc strict document validation")
        private boolean strictDiscoveryDocumentValidation;

        @Schema(description = "require https")
        private boolean requireHttps;

        @Schema(description = "enable debug mode")
        private boolean showDebugInformation;
    }
}


