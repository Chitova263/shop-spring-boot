package com.chitova.florist.outbound.accounts;

import com.chitova.elasticpathcloud.accounts.model.*;
import com.chitova.florist.outbound.authorization.ElasticPathCloudAuthorizationClient;
import com.chitova.florist.outbound.accounts.models.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ElasticPathCloudAccountsClient {

    private final WebClient.Builder webClientBuilder;
    private final ElasticPathCloudAuthorizationClient elasticPathCloudAuthorizationClient;
    private final String baseUrl;
    private final String createAccountPath;
    private final String getAccountPath;
    private final String getAccountsPath;
    private final String generateAccountManagementAuthorizationTokenPath;
    private final String getAccountAuthenticationSettingsPath;
    private final String getAccountAuthenticationRealmOidcProfilesPath;
    private final String getAccountAuthenticationRealmPasswordProfilesPath;
    private final String createUserAuthenticationInfoPath;
    private final String createUserAuthenticationPasswordProfilePath;
    private final String createOneTimePasswordTokenPath;


    public ElasticPathCloudAccountsClient(final WebClient.Builder webClientBuilder,
                                          final ElasticPathCloudAuthorizationClient elasticPathCloudAuthorizationClient,
                                          @Value("${elasticpathcloud.accounts.baseUrl}") final String baseUrl,
                                          @Value("${elasticpathcloud.accounts.createAccount.path}") final String createAccountPath,
                                          @Value("${elasticpathcloud.accounts.getAccount.path}") final String getAccountPath,
                                          @Value("${elasticpathcloud.accounts.getAccounts.path}") final String getAccountsPath,
                                          @Value("${elasticpathcloud.accounts.generateAccountManagementAuthorizationToken.path}") final String generateAccountManagementAuthorizationTokenPath,
                                          @Value("${elasticpathcloud.accounts.getAccountAuthenticationSettings.path}") final String getAccountAuthenticationSettingsPath,
                                          @Value("${elasticpathcloud.accounts.getAccountAuthenticationRealmOidcProfiles.path}") final String getAccountAuthenticationRealmOidcProfilesPath,
                                          @Value("${elasticpathcloud.accounts.getAccountAuthenticationRealmPasswordProfiles.path}") final String getAccountAuthenticationRealmPasswordProfilesPath,
                                          @Value("${elasticpathcloud.accounts.createUserAuthenticationInfo.path}") final String createUserAuthenticationInfoPath,
                                          @Value("${elasticpathcloud.accounts.createUserAuthenticationPasswordProfile.path}") final String createUserAuthenticationPasswordProfilePath,
                                          @Value("${elasticpathcloud.accounts.createOneTimePasswordToken.path}") final String createOneTimePasswordTokenPath
                                          ) {
        this.webClientBuilder = webClientBuilder;
        this.elasticPathCloudAuthorizationClient = elasticPathCloudAuthorizationClient;
        this.baseUrl = baseUrl;
        this.createAccountPath = createAccountPath;
        this.getAccountPath = getAccountPath;
        this.getAccountsPath = getAccountsPath;
        this.generateAccountManagementAuthorizationTokenPath = generateAccountManagementAuthorizationTokenPath;
        this.getAccountAuthenticationSettingsPath = getAccountAuthenticationSettingsPath;
        this.getAccountAuthenticationRealmOidcProfilesPath = getAccountAuthenticationRealmOidcProfilesPath;
        this.getAccountAuthenticationRealmPasswordProfilesPath = getAccountAuthenticationRealmPasswordProfilesPath;
        this.createUserAuthenticationInfoPath = createUserAuthenticationInfoPath;
        this.createUserAuthenticationPasswordProfilePath = createUserAuthenticationPasswordProfilePath;
        this.createOneTimePasswordTokenPath = createOneTimePasswordTokenPath;
    }

    public ElasticPathCloudAccountAuthenticationSettingsResponse getAccountAuthenticationSettings() {
        return this.webClientBuilder.clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getAccountAuthenticationSettingsPath)
                        .build()
                )
                .retrieve()
                .bodyToMono(ElasticPathCloudAccountAuthenticationSettingsResponse.class)
                .block();
    }

    public ElasticPathCloudAccountAuthenticationRealmOidcProfilesResponse getAccountAuthenticationRealmOidcProfilesResponse (final String authenticationRealmId) {
        return webClientBuilder.clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getAccountAuthenticationRealmOidcProfilesPath)
                        .build(authenticationRealmId))
                .retrieve()
                .bodyToMono(ElasticPathCloudAccountAuthenticationRealmOidcProfilesResponse.class)
                .block();
    }

    public ElasticPathCloudPasswordProfilesResponse getPasswordProfiles(final String authenticationRealmId) {
        return webClientBuilder.clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getAccountAuthenticationRealmPasswordProfilesPath)
                        .build(authenticationRealmId))
                .retrieve()
                .bodyToMono(ElasticPathCloudPasswordProfilesResponse.class)
                .block();
    }

    public ElasticPathCloudCreateUserAuthenticationInfoResponse createUserAuthenticationInfo (
            final String authenticationRealmId,
            final ElasticPathCloudCreateUserAuthenticationInfoRequest elasticPathCloudCreateUserAuthenticationInfo) {
        return webClientBuilder.clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(createUserAuthenticationInfoPath)
                        .build(authenticationRealmId))
                .bodyValue(elasticPathCloudCreateUserAuthenticationInfo)
                .retrieve()
                .bodyToMono(ElasticPathCloudCreateUserAuthenticationInfoResponse.class)
                .block();
    }

    // Associate user with a password profile
    public ElasticPathCloudCreateUserAuthenticationPasswordProfileResponse createUserAuthenticationPasswordProfile (
            final String authenticationRealmId,
            final String userAuthenticationInfoId,
            final ElasticPathCloudCreateUserAuthenticationPasswordProfileRequest request) {
        return webClientBuilder.clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(createUserAuthenticationPasswordProfilePath)
                        .build(authenticationRealmId, userAuthenticationInfoId))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ElasticPathCloudCreateUserAuthenticationPasswordProfileResponse.class)
                .block();
    }

    public void createOneTimePasswordToken(
            final String authenticationRealmId,
            final String passwordProfileId,
            final ElasticPathCloudCreateOneTimePasswordTokenRequest request) {
        webClientBuilder.clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(createOneTimePasswordTokenPath)
                        .build(authenticationRealmId, passwordProfileId))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    public ElasticPathCloudGenerateAccountManagementTokenResponse generateAccountManagementAuthorizationToken(
            final ElasticPathCloudGenerateAccountManagementTokenUsernameAndPasswordRequest request) {
        return webClientBuilder
                .clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(generateAccountManagementAuthorizationTokenPath)
                        .build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ElasticPathCloudGenerateAccountManagementTokenResponse.class)
                .block();
    }

    public PostV2Accounts201Response createAccount(final PostV2AccountsRequest request) {
        return webClientBuilder
                .clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(createAccountPath)
                        .build())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PostV2Accounts201Response.class)
                .block();
    }

    public GetV2Accounts200Response getAccount(final String accountId) {
        return webClientBuilder
                .clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getAccountPath)
                        .build(accountId))
                .retrieve()
                .bodyToMono(GetV2Accounts200Response.class)
                .block();
    }

    public GetV2Accounts200Response getAccounts() {
        return webClientBuilder
                .clone()
                .baseUrl(baseUrl)
                .filter(elasticPathCloudAuthorizationClient.addAuthorizationFilter())
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(getAccountsPath)
                        .build())
                .retrieve()
                .bodyToMono(GetV2Accounts200Response.class)
                .block();
    }


}
