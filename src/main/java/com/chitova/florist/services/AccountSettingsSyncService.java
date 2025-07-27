package com.chitova.florist.services;

import com.chitova.florist.outbound.accounts.ElasticPathCloudAccountsClient;
import com.chitova.florist.outbound.accounts.models.ElasticPathCloudAccountAuthenticationSettingsResponse;
import com.chitova.florist.outbound.accounts.models.ElasticPathCloudAccountAuthenticationRealmOidcProfilesResponse;
import com.chitova.florist.outbound.accounts.models.ElasticPathCloudPasswordProfilesResponse;
import com.chitova.florist.repositories.ConfigurationRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountSettingsSyncService {

    private final ElasticPathCloudAccountsClient elasticPathCloudAccountsClient;
    private final ConfigurationRepository configurationRepository;

    public AccountSettingsSyncService(final ElasticPathCloudAccountsClient elasticPathCloudAccountsClient, final ConfigurationRepository configurationRepository) {
        this.elasticPathCloudAccountsClient = elasticPathCloudAccountsClient;
        this.configurationRepository = configurationRepository;
    }

    public void syncAccountSettings() {
        ElasticPathCloudAccountAuthenticationSettingsResponse accountAuthenticationSettingsResponse = elasticPathCloudAccountsClient.getAccountAuthenticationSettings();
        System.out.println(accountAuthenticationSettingsResponse);

        //client id data meta client id

        String realmId = accountAuthenticationSettingsResponse.getData().getRelationships().getAuthenticationRealm().getData().getId();
        ElasticPathCloudAccountAuthenticationRealmOidcProfilesResponse oidcProfilesResponse = elasticPathCloudAccountsClient.getAccountAuthenticationRealmOidcProfiles(realmId);
        ElasticPathCloudPasswordProfilesResponse passwordProfilesResponse = elasticPathCloudAccountsClient.getPasswordProfiles(realmId);

        System.out.println(oidcProfilesResponse);
    }
}
