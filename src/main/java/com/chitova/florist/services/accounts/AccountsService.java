package com.chitova.florist.services.accounts;

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

    public AccountManagementTokenResponse registerAccount(final RegisterAccountRequest registerAccountRequest) {
        final ElasticPathCloudAccountAuthenticationSettingsResponse accountAuthenticationSettingsResponse = elasticPathCloudAccountsClient
                .getAccountAuthenticationSettings();
        final String authenticationRealmId = AccountsResponseAccessor.getAuthenticationRealmId(accountAuthenticationSettingsResponse);

        final ElasticPathCloudPasswordProfilesResponse passwordProfilesResponse = elasticPathCloudAccountsClient
                .getPasswordProfiles(authenticationRealmId);
        final ElasticPathCloudPasswordProfilesResponse.PasswordProfile floristPasswordProfile = AccountsResponseAccessor.getFloristPasswordProfile(passwordProfilesResponse);

        // User authentication info represents the user
        final ElasticPathCloudCreateUserAuthenticationInfoRequest createUserAuthenticationInfoRequest = AccountsMapper.getCreateUserAuthenticationInfoRequest(
                registerAccountRequest.getEmail(),
                registerAccountRequest.getPassword());
        final ElasticPathCloudCreateUserAuthenticationInfoResponse createUserAuthenticationInfoResponse = elasticPathCloudAccountsClient.createUserAuthenticationInfo(authenticationRealmId, createUserAuthenticationInfoRequest);
        final String userAuthenticationInfoId = createUserAuthenticationInfoResponse.getData().getId();

        // Associate the User Authenticated Info with a Password Profile
        final ElasticPathCloudCreateUserAuthenticationPasswordProfileRequest createUserAuthenticationPasswordProfileRequest = AccountsMapper.getUserAuthenticationPasswordProfileInfo(registerAccountRequest, floristPasswordProfile);

        elasticPathCloudAccountsClient.createUserAuthenticationPasswordProfile(authenticationRealmId, userAuthenticationInfoId, createUserAuthenticationPasswordProfileRequest);

        if (registerAccountRequest.isOneTimePasswordToken()) {
            final ElasticPathCloudCreateOneTimePasswordTokenRequest oneTimePasswordTokenRequest = AccountsMapper.createOneTimePasswordTokenRequest(registerAccountRequest.getEmail());
            elasticPathCloudAccountsClient.createOneTimePasswordToken(authenticationRealmId, floristPasswordProfile.getId(), oneTimePasswordTokenRequest);
            return AccountManagementTokenResponse.builder()
                    .oneTimePasswordToken(true)
                    .build();
        }

        // Receive account management token via email
        final ElasticPathCloudGenerateAccountManagementTokenUsernameAndPasswordRequest generateAccountManagementTokenUsernameAndPasswordRequest =
                AccountsMapper.createGenerateAccountManagementTokenUsernameAndPasswordRequest(registerAccountRequest.getEmail(),
                        registerAccountRequest.getPassword(),
                        floristPasswordProfile.getId()
        );
        final ElasticPathCloudGenerateAccountManagementTokenResponse generateAccountManagementTokenResponse = elasticPathCloudAccountsClient
                .generateAccountManagementAuthorizationToken(generateAccountManagementTokenUsernameAndPasswordRequest);

        final ElasticPathCloudGenerateAccountManagementTokenResponse.DataItem accountManagementData = AccountsResponseAccessor.getAccountManagementData(generateAccountManagementTokenResponse);
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
        final String authenticationRealmId = AccountsResponseAccessor.getAuthenticationRealmId(accountAuthenticationSettingsResponse);

        final ElasticPathCloudPasswordProfilesResponse passwordProfilesResponse = elasticPathCloudAccountsClient
                .getPasswordProfiles(authenticationRealmId);
        final ElasticPathCloudPasswordProfilesResponse.PasswordProfile floristPasswordProfile = AccountsResponseAccessor.getFloristPasswordProfile(passwordProfilesResponse);

        // Passwordless authentication using One Time Password Token
        if(request.isOneTimePasswordToken()){
            final ElasticPathCloudCreateOneTimePasswordTokenRequest oneTimePasswordTokenRequest = AccountsMapper.createOneTimePasswordTokenRequest(request.getEmail());
            elasticPathCloudAccountsClient.createOneTimePasswordToken(authenticationRealmId, floristPasswordProfile.getId(), oneTimePasswordTokenRequest);
            return AccountManagementTokenResponse.builder()
                    .oneTimePasswordToken(true)
                    .build();
        }

        // Receive account management token via email
        final ElasticPathCloudGenerateAccountManagementTokenUsernameAndPasswordRequest generateAccountManagementTokenUsernameAndPasswordRequest =
                AccountsMapper.createGenerateAccountManagementTokenUsernameAndPasswordRequest(request.getEmail(), request.getPassword(), floristPasswordProfile.getId());

        final ElasticPathCloudGenerateAccountManagementTokenResponse generateAccountManagementTokenResponse = elasticPathCloudAccountsClient
                .generateAccountManagementAuthorizationToken(generateAccountManagementTokenUsernameAndPasswordRequest);
        final ElasticPathCloudGenerateAccountManagementTokenResponse.DataItem accountManagementData = AccountsResponseAccessor.getAccountManagementData(generateAccountManagementTokenResponse);
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
