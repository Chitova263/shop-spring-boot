package com.chitova.florist.outbound.products.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ElasticPathNodeChildrenResponse {

    private List<NodeData> data;
    private Meta meta;

    @Data
    public static class NodeData {
        private String type;
        private String id;
        private Attributes attributes;
        private Relationships relationships;
        private NodeMeta meta;
    }

    @Data
    public static class Attributes {
        private String name;
        private String slug;
        private String description;
    }

    @Data
    public static class Relationships {
        private Children children;
        private Parent parent;
        private Products products;
    }

    @Data
    public static class Children {
        private List<ChildData> data;
        private Links links;
    }

    @Data
    public static class Parent {
        private ParentData data;
    }

    @Data
    public static class Products {
        private List<ProductData> data;
        private Links links;
    }

    @Data
    public static class ChildData {
        private String type;
        private String id;
    }

    @Data
    public static class ParentData {
        private String type;
        private String id;
    }

    @Data
    public static class ProductData {
        private String type;
        private String id;
    }

    @Data
    public static class Links {
        private String related;
    }

    @Data
    public static class NodeMeta {
        private List<Breadcrumb> breadcrumbs;

        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("has_children")
        private boolean hasChildren;

        @JsonProperty("hierarchy_id")
        private String hierarchyId;

        private String owner;

        @JsonProperty("sort_order")
        private int sortOrder;

        @JsonProperty("updated_at")
        private String updatedAt;
    }

    @Data
    public static class Breadcrumb {
        private String id;
        private String name;
        private String slug;
    }

    @Data
    public static class Meta {
        private Results results;
    }

    @Data
    public static class Results {
        private int total;
    }
}

