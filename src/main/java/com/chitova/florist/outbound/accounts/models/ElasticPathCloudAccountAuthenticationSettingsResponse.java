package com.chitova.florist.outbound.accounts.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ElasticPathCloudAccountAuthenticationSettingsResponse {

    private ElasticPathAccountSettings data;
    private Links links;

    @Data
    public static class ElasticPathAccountSettings {
        private String id;
        private String type;

        @JsonProperty("enable_self_signup")
        private boolean enableSelfSignup;

        @JsonProperty("auto_create_account_for_account_members")
        private boolean autoCreateAccountForAccountMembers;

        @JsonProperty("account_member_self_management")
        private String accountMemberSelfManagement;

        @JsonProperty("account_management_authentication_token_timeout_secs")
        private int accountManagementAuthenticationTokenTimeoutSecs;

        private Relationships relationships;
        private Meta meta;

        @Data
        public static class Relationships {

            @JsonProperty("authentication_realm")
            private AuthenticationRealm authenticationRealm;

            @Data
            public static class AuthenticationRealm {
                private RealmData data;

                @Data
                public static class RealmData {
                    private String id;
                    private String type;
                    private Links links;

                    @Data
                    public static class Links {
                        private String self;
                    }
                }
            }
        }

        @Data
        public static class Meta {

            @JsonProperty("client_id")
            private String clientId;
        }
    }

    @Data
    public static class Links {
        private String self;
    }
}
