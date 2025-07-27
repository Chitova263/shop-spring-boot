package com.chitova.florist.outbound.account.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ElasticPathCloudAccountAuthenticationRealmOidcProfilesResponse {

    private Meta meta;
    private List<OidcProfile> data;
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
    public static class OidcProfile {
        private String id;
        private String name;
        private String type;

        @JsonProperty("discovery_url")
        private String discoveryUrl;

        @JsonProperty("client_id")
        private String clientId;

        private Meta meta;
        private Links links;

        @Data
        public static class Meta {
            private String issuer;

            @JsonProperty("created_at")
            private String createdAt;

            @JsonProperty("updated_at")
            private String updatedAt;
        }

        @Data
        public static class Links {
            @JsonProperty("authorization-endpoint")
            private String authorizationEndpoint;

            @JsonProperty("client-discovery-url")
            private String clientDiscoveryUrl;

            @JsonProperty("callback-endpoint")
            private String callbackEndpoint;

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
