package com.chitova.florist.services.account;

import com.chitova.florist.model.account.GenerateAccountManagementTokenOidcRequest;
import com.chitova.florist.model.account.GenerateAccountManagementTokenRequest;
import com.chitova.florist.model.account.RegisterAccountRequest;
import com.chitova.florist.model.account.AccountManagementTokenResponse;
import com.chitova.florist.outbound.account.ElasticPathCloudAccountsClient;
import com.chitova.florist.outbound.account.models.*;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final ElasticPathCloudAccountsClient elasticPathCloudAccountsClient;

    public AccountService(final ElasticPathCloudAccountsClient elasticPathCloudAccountsClient) {
        this.elasticPathCloudAccountsClient = elasticPathCloudAccountsClient;
    }

    public AccountManagementTokenResponse registerAccount(final RegisterAccountRequest registerAccountRequest) {
        final ElasticPathCloudAccountAuthenticationSettingsResponse accountAuthenticationSettingsResponse = elasticPathCloudAccountsClient
                .getAccountAuthenticationSettings();
        final String authenticationRealmId = AccountResponseAccessor.getAuthenticationRealmId(accountAuthenticationSettingsResponse);

        final ElasticPathCloudPasswordProfilesResponse passwordProfilesResponse = elasticPathCloudAccountsClient
                .getPasswordProfiles(authenticationRealmId);
        final ElasticPathCloudPasswordProfilesResponse.PasswordProfile floristPasswordProfile = AccountResponseAccessor.getFloristPasswordProfile(passwordProfilesResponse);

        // User authentication info represents the user
        final ElasticPathCloudCreateUserAuthenticationInfoRequest createUserAuthenticationInfoRequest = AccountMapper.getCreateUserAuthenticationInfoRequest(
                registerAccountRequest.getEmail(),
                registerAccountRequest.getPassword());
        final ElasticPathCloudCreateUserAuthenticationInfoResponse createUserAuthenticationInfoResponse = elasticPathCloudAccountsClient.createUserAuthenticationInfo(authenticationRealmId, createUserAuthenticationInfoRequest);
        final String userAuthenticationInfoId = createUserAuthenticationInfoResponse.getData().getId();

        // Associate the User Authenticated Info with a Password Profile
        final ElasticPathCloudCreateUserAuthenticationPasswordProfileRequest createUserAuthenticationPasswordProfileRequest = AccountMapper.getUserAuthenticationPasswordProfileInfo(registerAccountRequest, floristPasswordProfile);

        elasticPathCloudAccountsClient.createUserAuthenticationPasswordProfile(authenticationRealmId, userAuthenticationInfoId, createUserAuthenticationPasswordProfileRequest);

        if (registerAccountRequest.isOneTimePasswordToken()) {
            final ElasticPathCloudCreateOneTimePasswordTokenRequest oneTimePasswordTokenRequest = AccountMapper.createOneTimePasswordTokenRequest(registerAccountRequest.getEmail());
            elasticPathCloudAccountsClient.createOneTimePasswordToken(authenticationRealmId, floristPasswordProfile.getId(), oneTimePasswordTokenRequest);
            return AccountManagementTokenResponse.builder()
                    .oneTimePasswordToken(true)
                    .build();
        }

        // Receive account management token via email
        final ElasticPathCloudGenerateAccountManagementTokenUsernameAndPasswordRequest generateAccountManagementTokenUsernameAndPasswordRequest =
                AccountMapper.createGenerateAccountManagementTokenUsernameAndPasswordRequest(registerAccountRequest.getEmail(),
                        registerAccountRequest.getPassword(),
                        floristPasswordProfile.getId()
        );
        final ElasticPathCloudGenerateAccountManagementTokenResponse generateAccountManagementTokenResponse = elasticPathCloudAccountsClient
                .generateAccountManagementAuthorizationTokenUsernamePassword(generateAccountManagementTokenUsernameAndPasswordRequest);

        final ElasticPathCloudGenerateAccountManagementTokenResponse.DataItem accountManagementData = AccountResponseAccessor.getAccountManagementData(generateAccountManagementTokenResponse);
        return AccountManagementTokenResponse.builder()
                .accountMemberId(generateAccountManagementTokenResponse.getMeta().getAccountMemberId())
                .accountId(accountManagementData.getAccountId())
                .accountName(accountManagementData.getAccountName())
                .token(accountManagementData.getToken())
                .tokenType(accountManagementData.getType())
                .tokenExpiry(accountManagementData.getExpires())
                .build();
    }

    public AccountManagementTokenResponse generateAccountManagementToken(final GenerateAccountManagementTokenOidcRequest request){
        final ElasticPathCloudGenerateAccountManagementTokenOIDCRequest generateAccountManagementTokenOIDCRequest = ElasticPathCloudGenerateAccountManagementTokenOIDCRequest.builder()
                .data(ElasticPathCloudGenerateAccountManagementTokenOIDCRequest.AuthTokenData.builder()
                        .type("account_management_authentication_token")
                        .authenticationMechanism("oidc")
                        .oauthCodeVerifier(request.getOauthCodeVerifier())
                        .oauthRedirectUri(request.getOauthRedirectUri())
                        .oauthAuthorizationCode(request.getOauthAuthorizationCode())
                        .build())
                .build();
        final ElasticPathCloudGenerateAccountManagementTokenResponse generateAccountManagementTokenResponse = elasticPathCloudAccountsClient.generateAccountManagementTokenOidc(generateAccountManagementTokenOIDCRequest);
        final ElasticPathCloudGenerateAccountManagementTokenResponse.DataItem accountManagementData = AccountResponseAccessor.getAccountManagementData(generateAccountManagementTokenResponse);
        return AccountManagementTokenResponse.builder()
                .accountMemberId(generateAccountManagementTokenResponse.getMeta().getAccountMemberId())
                .accountId(accountManagementData.getAccountId())
                .accountName(accountManagementData.getAccountName())
                .token(accountManagementData.getToken())
                .tokenType(accountManagementData.getType())
                .tokenExpiry(accountManagementData.getExpires())
                .build();
    }


    public AccountManagementTokenResponse generateAccountManagementToken(final GenerateAccountManagementTokenRequest request) {
        final ElasticPathCloudAccountAuthenticationSettingsResponse accountAuthenticationSettingsResponse = elasticPathCloudAccountsClient
                .getAccountAuthenticationSettings();
        final String authenticationRealmId = AccountResponseAccessor.getAuthenticationRealmId(accountAuthenticationSettingsResponse);

        final ElasticPathCloudPasswordProfilesResponse passwordProfilesResponse = elasticPathCloudAccountsClient
                .getPasswordProfiles(authenticationRealmId);
        final ElasticPathCloudPasswordProfilesResponse.PasswordProfile floristPasswordProfile = AccountResponseAccessor.getFloristPasswordProfile(passwordProfilesResponse);

        // Passwordless authentication using One Time Password Token
        if(request.isOneTimePasswordToken()){
            final ElasticPathCloudCreateOneTimePasswordTokenRequest oneTimePasswordTokenRequest = AccountMapper.createOneTimePasswordTokenRequest(request.getEmail());
            elasticPathCloudAccountsClient.createOneTimePasswordToken(authenticationRealmId, floristPasswordProfile.getId(), oneTimePasswordTokenRequest);
            return AccountManagementTokenResponse.builder()
                    .oneTimePasswordToken(true)
                    .build();
        }

        // Receive account management token via email
        final ElasticPathCloudGenerateAccountManagementTokenUsernameAndPasswordRequest generateAccountManagementTokenUsernameAndPasswordRequest =
                AccountMapper.createGenerateAccountManagementTokenUsernameAndPasswordRequest(request.getEmail(), request.getPassword(), floristPasswordProfile.getId());

        final ElasticPathCloudGenerateAccountManagementTokenResponse generateAccountManagementTokenResponse = elasticPathCloudAccountsClient
                .generateAccountManagementAuthorizationTokenUsernamePassword(generateAccountManagementTokenUsernameAndPasswordRequest);
        final ElasticPathCloudGenerateAccountManagementTokenResponse.DataItem accountManagementData = AccountResponseAccessor.getAccountManagementData(generateAccountManagementTokenResponse);
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
