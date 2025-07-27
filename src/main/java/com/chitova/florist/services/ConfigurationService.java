package com.chitova.florist.services;

import com.chitova.florist.model.configuration.GetConfigurationResponse;
import com.chitova.florist.repositories.ConfigurationRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
public class ConfigurationService {
    private final ConfigurationRepository configurationRepository;

    public ConfigurationService(final ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Cacheable("configuration")
    public GetConfigurationResponse getConfiguration() {
        return configurationRepository.findAll()
                .stream()
                .map(configuration -> GetConfigurationResponse.builder()
                        .baseUrl(configuration.getBaseUrl())
                        .openIdConnect(configuration.getOpenIdConnectConfiguration().stream()
                                .map(openIdConnectConfiguration -> GetConfigurationResponse.OpenIdConnectConfiguration.builder()
                                        .issuer(openIdConnectConfiguration.getIssuer())
                                        .scope(openIdConnectConfiguration.getScope())
                                        .clientSecret(openIdConnectConfiguration.getClientSecret())
                                        .provider(openIdConnectConfiguration.getProvider())
                                        .redirectUri(openIdConnectConfiguration.getRedirectUri())
                                        .responseType(openIdConnectConfiguration.getResponseType())
                                        .showDebugInformation(openIdConnectConfiguration.isShowDebugInformation())
                                        .strictDiscoveryDocumentValidation(openIdConnectConfiguration.isStrictDiscoveryDocumentValidation())
                                        .clientId(openIdConnectConfiguration.getClientId())
                                        .requireHttps(openIdConnectConfiguration.isRequireHttps())
                                        .build())
                                .collect(Collectors.toSet()))
                        .build())
                .findFirst()
                .orElse(null);
    }
}
