package com.chitova.florist.outbound.account.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ElasticPathCloudCreateUserAuthenticationPasswordProfileRequest {

    private UserPasswordProfileInfo data;

    @Builder
    @Data
    public static class UserPasswordProfileInfo {
        private String type;

        @JsonProperty("password_profile_id")
        private String passwordProfileId;

        private String username;
        private String password;
    }
}
