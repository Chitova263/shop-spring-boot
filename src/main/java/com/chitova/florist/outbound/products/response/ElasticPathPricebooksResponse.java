package com.chitova.florist.outbound.products.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ElasticPathPricebooksResponse {
    private List<PricebookData> data;
    private Links links;
    private Meta meta;

    @Data
    public static class PricebookData {
        private String id;
        private String type;
        private Attributes attributes;
        private OwnerMeta meta;
    }

    @Data
    public static class Attributes {
        @JsonProperty("created_at")
        private String createdAt;

        private String description;
        private String name;

        @JsonProperty("updated_at")
        private String updatedAt;
    }

    @Data
    public static class OwnerMeta {
        private String owner;
    }

    @Data
    public static class Links {
        private String first;
        private String last;
        private String self;
    }

    @Data
    public static class Meta {
        private Page page;
        private Results results;
    }

    @Data
    public static class Page {
        private int current;
        private int limit;
        private int offset;
        private int total;
    }

    @Data
    public static class Results {
        private int total;
    }
}
