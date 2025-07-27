package com.chitova.florist.services.accounts;

import com.chitova.florist.outbound.accounts.models.ElasticPathCloudAccountAuthenticationSettingsResponse;
import com.chitova.florist.outbound.accounts.models.ElasticPathCloudGenerateAccountManagementTokenResponse;
import com.chitova.florist.outbound.accounts.models.ElasticPathCloudPasswordProfilesResponse;

public final class AccountsResponseAccessor {
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
