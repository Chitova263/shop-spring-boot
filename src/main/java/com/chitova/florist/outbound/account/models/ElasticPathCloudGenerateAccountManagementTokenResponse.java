package com.chitova.florist.outbound.account.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ElasticPathCloudGenerateAccountManagementTokenResponse {

    private Meta meta;
    private List<DataItem> data;
    private Links links;

    @Data
    @Builder
    public static class Meta {
        @JsonProperty("account_member_id")
        private String accountMemberId;

        private Page page;
    }

    @Data
    @Builder
    public static class Page {
        private int limit;
        private int offset;
        private int current;
        private int total;
    }

//    @Data
//    @Builder
//    public static class Results {
//        private int total;
//    }

    @Data
    @Builder
    public static class DataItem {
        @JsonProperty("account_name")
        private String accountName;

        @JsonProperty("account_id")
        private String accountId;

        private String token;
        private String type;
        private String expires;
    }

    @Data
    @Builder
    public static class Links {
        private String current;
        private String first;
        private String last;
        private String next;
        private String prev;
    }
}
