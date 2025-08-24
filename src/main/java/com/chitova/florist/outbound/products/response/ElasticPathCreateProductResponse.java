package com.chitova.florist.outbound.products.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ElasticPathCreateProductResponse {

    private DataNode data;

    @Data
    public static class DataNode {
        private String type;
        private String id;
        private Attributes attributes;
        private Relationships relationships;
        private Meta meta;
    }

    @Data
    public static class Attributes {
        @JsonProperty("commodity_type")
        private String commodityType;
        private String description;
        private String mpn;
        private String name;
        private String sku;
        private String slug;
        private String status;
        @JsonProperty("upc_ean")
        private String upcEan;
    }

    @Data
    public static class Relationships {
        private Relationship children;
        @JsonProperty("component_products")
        private Relationship componentProducts;
        @JsonProperty("custom_relationships")
        private Relationship customRelationships;
        private Relationship files;
        @JsonProperty("main_image")
        private Relationship mainImage;
        private Relationship templates;
        private Relationship variations;
    }

    @Data
    public static class Relationship {
        private List<RelationshipData> data;
        private Links links;
    }

    @Data
    public static class RelationshipData {
        private String type;
        private String id;
    }

    @Data
    public static class Links {
        private String self;
    }

    @Data
    public static class Meta {
        @JsonProperty("created_at")
        private String createdAt;
        private String owner;
        @JsonProperty("product_types")
        private List<String> productTypes;
        @JsonProperty("updated_at")
        private String updatedAt;
    }
}
