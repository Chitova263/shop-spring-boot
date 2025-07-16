package com.chitova.florist.outbound.accounts.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ElasticPathCloudPasswordProfilesResponse {

    private Meta meta;
    private List<PasswordProfile> data;
    private Links links;

    @Data
    public static class Meta {
        private Page page;
        private Results results;

        @Data
        public static class Page {
            private int limit;
            private int offset;
            private int current;
            private int total;
        }

        @Data
        public static class Results {
            private int total;
        }
    }

    @Data
    public static class PasswordProfile {
        private String id;
        private String name;
        private String type;

        @JsonProperty("username_format")
        private String usernameFormat;

        @JsonProperty("enable_one_time_password_token")
        private boolean enableOneTimePasswordToken;

        private Meta meta;
        private Links links;

        @Data
        public static class Meta {
            @JsonProperty("created_at")
            private String createdAt;

            @JsonProperty("updated_at")
            private String updatedAt;
        }

        @Data
        public static class Links {
            private String self;
        }
    }

    @Data
    public static class Links {
        private String first;
        private String last;
        private String prev;
        private String next;
        private String current;
    }
}
