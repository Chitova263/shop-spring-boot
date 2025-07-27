package com.chitova.florist.services.accounts;

import com.chitova.florist.model.accounts.RegisterAccountRequest;
import com.chitova.florist.outbound.accounts.models.*;

public final class AccountsMapper {
    public static ElasticPathCloudCreateOneTimePasswordTokenRequest createOneTimePasswordTokenRequest(final String email) {
        return ElasticPathCloudCreateOneTimePasswordTokenRequest.builder()
                .data(ElasticPathCloudCreateOneTimePasswordTokenRequest.OtpTokenRequestData.builder()
                        .purpose("passwordless_authentication")
                        .type("one_time_password_token_request")
                        .username(email)
                        .build())
                .build();
    }

    public static ElasticPathCloudCreateUserAuthenticationPasswordProfileRequest getUserAuthenticationPasswordProfileInfo(RegisterAccountRequest registerAccountRequest, ElasticPathCloudPasswordProfilesResponse.PasswordProfile floristPasswordProfile) {
        return ElasticPathCloudCreateUserAuthenticationPasswordProfileRequest.builder()
                .data(ElasticPathCloudCreateUserAuthenticationPasswordProfileRequest.UserPasswordProfileInfo.builder()
                        .username(registerAccountRequest.getEmail())
                        .password(registerAccountRequest.getPassword())
                        .type("user_authentication_password_profile_info")
                        .passwordProfileId(floristPasswordProfile.getId())
                        .build())
                .build();
    }

    public static ElasticPathCloudCreateUserAuthenticationInfoRequest getCreateUserAuthenticationInfoRequest(
            final String email,
            final String password
    ) {
        return ElasticPathCloudCreateUserAuthenticationInfoRequest.builder()
                .data(ElasticPathCloudCreateUserAuthenticationInfoRequest.UserAuthenticationInfo.builder()
                        .email(email)
                        .name(password)
                        .type("user_authentication_info")
                        .build())
                .build();
    }

    public static ElasticPathCloudGenerateAccountManagementTokenUsernameAndPasswordRequest createGenerateAccountManagementTokenUsernameAndPasswordRequest(final String email,
                                                                                                                                                           final String password,
                                                                                                                                                           final String floristPasswordProfileId) {
        return ElasticPathCloudGenerateAccountManagementTokenUsernameAndPasswordRequest.builder()
                .data(ElasticPathCloudGenerateAccountManagementTokenUsernameAndPasswordRequest.DataPayload.builder()
                        .type("account_management_authentication_token")
                        .authenticationMechanism("password")
                        .username(email)
                        .password(password)
                        .passwordProfileId(floristPasswordProfileId)
                        .build())
                .build();
    }
}
