package com.chitova.florist.outbound.accounts.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ElasticPathCloudCreateUserAuthenticationPasswordProfileResponse {

    private UserPasswordProfileInfo data;
    private Links links;

    @Data
    public static class UserPasswordProfileInfo {
        private String id;
        private String username;
        private String type;

        @JsonProperty("password_profile_id")
        private String passwordProfileId;

        private Meta meta;

        @Data
        public static class Meta {
            @JsonProperty("created_at")
            private String createdAt;

            @JsonProperty("updated_at")
            private String updatedAt;
        }
    }

    @Data
    public static class Links {
        private String self;
    }
}
