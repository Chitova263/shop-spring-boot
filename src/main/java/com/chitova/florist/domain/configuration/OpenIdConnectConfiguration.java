package com.chitova.florist.domain.configuration;

import lombok.Getter;

@Getter
public class OpenIdConnectConfiguration {
    private String issuer;
    private String provider;
    private String redirectUri;
    private String clientId;
    private String clientSecret;
    private String responseType;
    private String scope;
    private String authorizationUrl;
    private boolean requireHttps;
    private boolean strictDiscoveryDocumentValidation;
    private boolean showDebugInformation;
}
