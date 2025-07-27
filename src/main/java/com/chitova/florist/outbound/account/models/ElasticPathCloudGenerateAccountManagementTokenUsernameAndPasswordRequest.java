package com.chitova.florist.outbound.account.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ElasticPathCloudGenerateAccountManagementTokenUsernameAndPasswordRequest {

    private DataPayload data;

    @Data
    @Builder
    public static class DataPayload {
        private String type;

        @JsonProperty("authentication_mechanism")
        private String authenticationMechanism;

        @JsonProperty("password_profile_id")
        private String passwordProfileId;

        private String username;
        private String password;
    }
}