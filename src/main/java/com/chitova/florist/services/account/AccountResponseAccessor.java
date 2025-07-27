package com.chitova.florist.services.account;

import com.chitova.florist.outbound.account.models.ElasticPathCloudAccountAuthenticationSettingsResponse;
import com.chitova.florist.outbound.account.models.ElasticPathCloudGenerateAccountManagementTokenResponse;
import com.chitova.florist.outbound.account.models.ElasticPathCloudPasswordProfilesResponse;

public final class AccountResponseAccessor {
    public static String getAuthenticationRealmId(final ElasticPathCloudAccountAuthenticationSettingsResponse accountAuthenticationSettingsResponse) {
        final String authenticationRealmId = accountAuthenticationSettingsResponse.getData()
                .getRelationships()
                .getAuthenticationRealm()
                .getData()
                .getId();
        return authenticationRealmId;
    }

    public static ElasticPathCloudPasswordProfilesResponse.PasswordProfile getFloristPasswordProfile(final ElasticPathCloudPasswordProfilesResponse passwordProfilesResponse) {
        return passwordProfilesResponse.getData()
                .stream()
                .filter(profileResponse -> profileResponse.getName().equals("Florist"))
                .findFirst()
                .get();
    }

    public static ElasticPathCloudGenerateAccountManagementTokenResponse.DataItem getAccountManagementData(final ElasticPathCloudGenerateAccountManagementTokenResponse generateAccountManagementTokenResponse) {
        return generateAccountManagementTokenResponse
                .getData()
                .stream()
                .findFirst()
                .get();
    }
}
