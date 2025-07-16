package com.chitova.florist.services;

import com.chitova.florist.model.accounts.GenerateAccountManagementTokenRequest;
import com.chitova.florist.model.accounts.RegisterAccountRequest;
import com.chitova.florist.model.accounts.AccountManagementTokenResponse;
import com.chitova.florist.outbound.accounts.ElasticPathCloudAccountsClient;
import com.chitova.florist.outbound.accounts.models.*;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

    private final ElasticPathCloudAccountsClient elasticPathCloudAccountsClient;

    public AccountsService(final ElasticPathCloudAccountsClient elasticPathCloudAccountsClient) {
        this.elasticPathCloudAccountsClient = elasticPathCloudAccountsClient;
    }

    public AccountManagementTokenResponse registerAccount(RegisterAccountRequest registerAccountRequest) {
        final ElasticPathCloudAccountAuthenticationSettingsResponse accountAuthenticationSettingsResponse = elasticPathCloudAccountsClient
                .getAccountAuthenticationSettings();
        final String authenticationRealmId = accountAuthenticationSettingsResponse.getData()
                .getRelationships()
                .getAuthenticationRealm()
                .getData()
                .getId();

        final ElasticPathCloudPasswordProfilesResponse passwordProfilesResponse = elasticPathCloudAccountsClient
                .getPasswordProfiles(authenticationRealmId);
        final ElasticPathCloudPasswordProfilesResponse.PasswordProfile floristPasswordProfile = passwordProfilesResponse.getData()
                .stream()
                .filter(profileResponse -> profileResponse.getName().equals("Florist"))
                .findFirst()
                .get();

        final ElasticPathCloudCreateUserAuthenticationInfoRequest createUserAuthenticationInfoRequest = ElasticPathCloudCreateUserAuthenticationInfoRequest.builder()
                .data(ElasticPathCloudCreateUserAuthenticationInfoRequest.UserAuthenticationInfo.builder()
                        .email(registerAccountRequest.getEmail())
                        .name(registerAccountRequest.getName())
                        .type(registerAccountRequest.getType())
                        .build())
                .build();

        // User authentication info represents the user
        final ElasticPathCloudCreateUserAuthenticationInfoResponse createUserAuthenticationInfoResponse = elasticPathCloudAccountsClient
                .createUserAuthenticationInfo(authenticationRealmId, createUserAuthenticationInfoRequest);
        final String userAuthenticationInfoId = createUserAuthenticationInfoResponse.getData().getId();

        // Associate the User Authenticated Info with a Password Profile
        final ElasticPathCloudCreateUserAuthenticationPasswordProfileRequest createUserAuthenticationPasswordProfileRequest = ElasticPathCloudCreateUserAuthenticationPasswordProfileRequest.builder()
                .data(ElasticPathCloudCreateUserAuthenticationPasswordProfileRequest.UserPasswordProfileInfo.builder()
                        .username(registerAccountRequest.getEmail())
                        .password(registerAccountRequest.getPassword())
                        .type("user_authentication_password_profile_info")
                        .passwordProfileId(floristPasswordProfile.getId())
                        .build())
                .build();

        final ElasticPathCloudCreateUserAuthenticationPasswordProfileResponse createUserAuthenticationPasswordProfileResponse = elasticPathCloudAccountsClient
                .createUserAuthenticationPasswordProfile(authenticationRealmId,
                        userAuthenticationInfoId,
                        createUserAuthenticationPasswordProfileRequest);

        if (registerAccountRequest.isOneTimePasswordToken()) {
            ElasticPathCloudCreateOneTimePasswordTokenRequest oneTimePasswordTokenRequest = ElasticPathCloudCreateOneTimePasswordTokenRequest.builder()
                    .data(ElasticPathCloudCreateOneTimePasswordTokenRequest.OtpTokenRequestData.builder()
                            .purpose("passwordless_authentication")
                            .type("one_time_password_token_request")
                            .username(registerAccountRequest.getEmail())
                            .build())
                    .build();
            elasticPathCloudAccountsClient.createOneTimePasswordToken(authenticationRealmId,
                    floristPasswordProfile.getId(),
                    oneTimePasswordTokenRequest
            );
            return AccountManagementTokenResponse.builder()
                    .oneTimePasswordToken(true)
                    .build();
        }

        // Receive account management token via email
        final ElasticPathCloudGenerateAccountManagementTokenUsernameAndPasswordRequest generateAccountManagementTokenUsernameAndPasswordRequest = ElasticPathCloudGenerateAccountManagementTokenUsernameAndPasswordRequest.builder()
                .data(ElasticPathCloudGenerateAccountManagementTokenUsernameAndPasswordRequest.DataPayload.builder()
                        .type("account_management_authentication_token")
                        .authenticationMechanism("password")
                        .username(registerAccountRequest.getEmail())
                        .password(registerAccountRequest.getPassword())
                        .passwordProfileId(floristPasswordProfile.getId())
                        .build())
                .build();

        final ElasticPathCloudGenerateAccountManagementTokenResponse generateAccountManagementTokenResponse = elasticPathCloudAccountsClient
                .generateAccountManagementAuthorizationToken(generateAccountManagementTokenUsernameAndPasswordRequest);

        final ElasticPathCloudGenerateAccountManagementTokenResponse.DataItem accountManagementData = generateAccountManagementTokenResponse.getData().stream().findFirst().get();

        return AccountManagementTokenResponse.builder()
                .accountMemberId(generateAccountManagementTokenResponse.getMeta().getAccountMemberId())
                .accountId(accountManagementData.getAccountId())
                .accountName(accountManagementData.getAccountName())
                .token(accountManagementData.getToken())
                .tokenType(accountManagementData.getType())
                .tokenExpiry(accountManagementData.getExpires())
                .build();
    }

    public AccountManagementTokenResponse generateAccountManagementToken(GenerateAccountManagementTokenRequest generateAccountManagementTokenRequest) {
        final ElasticPathCloudAccountAuthenticationSettingsResponse accountAuthenticationSettingsResponse = elasticPathCloudAccountsClient
                .getAccountAuthenticationSettings();
        final String authenticationRealmId = accountAuthenticationSettingsResponse.getData()
                .getRelationships()
                .getAuthenticationRealm()
                .getData()
                .getId();

        final ElasticPathCloudPasswordProfilesResponse passwordProfilesResponse = elasticPathCloudAccountsClient
                .getPasswordProfiles(authenticationRealmId);
        final ElasticPathCloudPasswordProfilesResponse.PasswordProfile floristPasswordProfile = passwordProfilesResponse.getData()
                .stream()
                .filter(profileResponse -> profileResponse.getName().equals("Florist"))
                .findFirst()
                .get();

        // Passwordless authentication
        if(generateAccountManagementTokenRequest.isOneTimePasswordToken()){
            ElasticPathCloudCreateOneTimePasswordTokenRequest oneTimePasswordTokenRequest = ElasticPathCloudCreateOneTimePasswordTokenRequest.builder()
                    .data(ElasticPathCloudCreateOneTimePasswordTokenRequest.OtpTokenRequestData.builder()
                            .purpose("passwordless_authentication")
                            .type("one_time_password_token_request")
                            .username(generateAccountManagementTokenRequest.getEmail())
                            .build())
                    .build();
            elasticPathCloudAccountsClient.createOneTimePasswordToken(authenticationRealmId,
                    floristPasswordProfile.getId(),
                    oneTimePasswordTokenRequest
            );
            return AccountManagementTokenResponse.builder()
                    .oneTimePasswordToken(true)
                    .build();
        }

        // Receive account management token via email
        final ElasticPathCloudGenerateAccountManagementTokenUsernameAndPasswordRequest generateAccountManagementTokenUsernameAndPasswordRequest = ElasticPathCloudGenerateAccountManagementTokenUsernameAndPasswordRequest.builder()
                .data(ElasticPathCloudGenerateAccountManagementTokenUsernameAndPasswordRequest.DataPayload.builder()
                        .type("account_management_authentication_token")
                        .authenticationMechanism("password")
                        .username(generateAccountManagementTokenRequest.getEmail())
                        .password(generateAccountManagementTokenRequest.getPassword())
                        .passwordProfileId(floristPasswordProfile.getId())
                        .build())
                .build();

        final ElasticPathCloudGenerateAccountManagementTokenResponse generateAccountManagementTokenResponse = elasticPathCloudAccountsClient
                .generateAccountManagementAuthorizationToken(generateAccountManagementTokenUsernameAndPasswordRequest);

        final ElasticPathCloudGenerateAccountManagementTokenResponse.DataItem accountManagementData = generateAccountManagementTokenResponse.getData().stream().findFirst().get();

        return AccountManagementTokenResponse.builder()
                .accountMemberId(generateAccountManagementTokenResponse.getMeta().getAccountMemberId())
                .accountId(accountManagementData.getAccountId())
                .accountName(accountManagementData.getAccountName())
                .token(accountManagementData.getToken())
                .tokenType(accountManagementData.getType())
                .tokenExpiry(accountManagementData.getExpires())
                .build();
    }
}
