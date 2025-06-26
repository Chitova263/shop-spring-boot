package com.chitova.florist.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ElasticPathCloudVariationsResponse {

    @JsonProperty("data")
    private List<ProductVariation> data;

    @JsonProperty("meta")
    private Meta meta;

    @Data
    @NoArgsConstructor
    public static class ProductVariation {

        @JsonProperty("type")
        private String type;

        @JsonProperty("id")
        private String id;

        @JsonProperty("attributes")
        private VariationAttributes attributes;

        @JsonProperty("meta")
        private VariationMeta meta;
    }

    @Data
    @NoArgsConstructor
    public static class VariationAttributes {

        @JsonProperty("name")
        private String name;
    }

    @Data
    @NoArgsConstructor
    public static class VariationMeta {

        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("updated_at")
        private String updatedAt;

        @JsonProperty("options")
        private List<VariationOption> options;

        @JsonProperty("owner")
        private String owner;
    }

    @Data
    @NoArgsConstructor
    public static class VariationOption {

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;

        @JsonProperty("sort_order")
        private int sortOrder;

        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("updated_at")
        private String updatedAt;
    }

    @Data
    @NoArgsConstructor
    public static class Meta {

        @JsonProperty("results")
        private Results results;
    }

    @Data
    @NoArgsConstructor
    public static class Results {

        @JsonProperty("total")
        private int total;
    }
}
