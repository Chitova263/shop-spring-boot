package com.chitova.florist.entities.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
