package com.chitova.florist.outbound.products.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ElasticPathCloudNodeProductsResponse {

    @JsonProperty("data")
    private List<Product> data;

    @JsonProperty("included")
    private Included included;

    @JsonProperty("meta")
    private Meta meta;

    @Data
    @NoArgsConstructor
    public static class Product {

        @JsonProperty("id")
        private String id;

        @JsonProperty("type")
        private String type;

        @JsonProperty("attributes")
        private Attributes attributes;

        @JsonProperty("meta")
        private ProductMeta meta;

        @JsonProperty("relationships")
        private Map<String, Object> relationships;
    }

    // Attributes class for product attributes
    @Data
    @NoArgsConstructor
    public static class Attributes {

        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;

        @JsonProperty("slug")
        private String slug;

        @JsonProperty("sku")
        private String sku;

        @JsonProperty("status")
        private String status;

        @JsonProperty("commodity_type")
        private String commodityType;

        @JsonProperty("upc_ean")
        private String upcEan;

        @JsonProperty("mpn")
        private String mpn;

        @JsonProperty("external_ref")
        private String externalRef;

        @JsonProperty("locales")
        private Map<String, Object> locales;

        @JsonProperty("tags")
        private List<String> tags;

        @JsonProperty("extensions")
        private Map<String, Object> extensions;

        @JsonProperty("custom_inputs")
        private Map<String, Object> customInputs;

        @JsonProperty("build_rules")
        private BuildRules buildRules;

        @JsonProperty("components")
        private Map<String, Object> components;
    }

    @Data
    @NoArgsConstructor
    public static class BuildRules {

        @JsonProperty("default")
        private String defaultRule;

        @JsonProperty("include")
        private List<List<String>> include;

        @JsonProperty("exclude")
        private List<List<String>> exclude;
    }

    @Data
    @NoArgsConstructor
    public static class ProductMeta {

        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("updated_at")
        private String updatedAt;

        @JsonProperty("owner")
        private String owner;

        @JsonProperty("variations")
        private List<Variation> variations;

        @JsonProperty("custom_relationships")
        private List<Object> customRelationships;

        @JsonProperty("child_variations")
        private List<ChildVariation> childVariations;

        @JsonProperty("product_types")
        private List<String> productTypes;

        @JsonProperty("variation_matrix")
        private Map<String, Object> variationMatrix;
    }

    @Data
    @NoArgsConstructor
    public static class Variation {

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("options")
        private List<Option> options;
    }

    @Data
    @NoArgsConstructor
    public static class ChildVariation {

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("sort_order")
        private int sortOrder;

        @JsonProperty("options")
        private List<Option> options;

        @JsonProperty("option")
        private Option option;
    }

    @Data
    @NoArgsConstructor
    public static class Option {

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;
    }

    @Data
    @NoArgsConstructor
    public static class Included {

        @JsonProperty("main_images")
        private List<File> mainImages;

        @JsonProperty("component_products")
        private List<Product> componentProducts;

        @JsonProperty("files")
        private List<File> files;
    }

    @Data
    @NoArgsConstructor
    public static class File {

        @JsonProperty("id")
        private String id;

        @JsonProperty("type")
        private String type;

        @JsonProperty("file_name")
        private String fileName;

        @JsonProperty("mime_type")
        private String mimeType;

        @JsonProperty("file_size")
        private int fileSize;

        @JsonProperty("public")
        private boolean isPublic;

        @JsonProperty("meta")
        private FileMeta meta;

        @JsonProperty("links")
        private Link links;

        @JsonProperty("link")
        private MetaLink link;
    }

    @Data
    @NoArgsConstructor
    public static class FileMeta {

        @JsonProperty("timestamps")
        private Map<String, String> timestamps;

        @JsonProperty("dimensions")
        private Dimensions dimensions;
    }

    @Data
    @NoArgsConstructor
    public static class Dimensions {

        @JsonProperty("width")
        private int width;

        @JsonProperty("height")
        private int height;
    }

    @Data
    @NoArgsConstructor
    public static class Link {

        @JsonProperty("self")
        private String self;
    }

    @Data
    @NoArgsConstructor
    public static class MetaLink {

        @JsonProperty("href")
        private String href;

        @JsonProperty("meta")
        private Map<String, Object> meta;
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

