

package com.chitova.florist.outbound.accounts.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ElasticPathCloudCreateUserAuthenticationInfoResponse {

    private UserAuthenticationInfo data;
    private Links links;

    @Data
    public static class UserAuthenticationInfo {
        private String id;
        private String name;
        private String email;
        private String type;
        private Meta meta;

        @Data
        public static class Meta {
            @JsonProperty("created_at")
            private String createdAt;

            @JsonProperty("updated_at")
            private String updatedAt;

            @JsonProperty("creation_status")
            private String creationStatus;
        }
    }

    @Data
    public static class Links {
        private String self;
    }
}
