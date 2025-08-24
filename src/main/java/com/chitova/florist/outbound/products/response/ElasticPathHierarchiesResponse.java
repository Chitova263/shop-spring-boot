package com.chitova.florist.outbound.products.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ElasticPathHierarchiesResponse {

    private List<DataItem> data;
    private Meta meta;

    @Data
    public static class DataItem {
        private String type;
        private String id;
        private Attributes attributes;
        private Relationships relationships;
        private MetaInfo meta;

        @Data
        public static class Attributes {
            private String name;
            private String slug;
        }

        @Data
        public static class Relationships {
            private Children children;

            @Data
            public static class Children {
                private List<DataItem> data;
                private Links links;

                @Data
                public static class Links {
                    private String related;
                }
            }
        }

        @Data
        public static class MetaInfo {
            private List<Breadcrumb> breadcrumbs;

            @JsonProperty("created_at")
            private String createdAt;

            @JsonProperty("has_children")
            private boolean hasChildren;

            private String owner;

            @JsonProperty("updated_at")
            private String updatedAt;

            @Data
            public static class Breadcrumb {
                private String id;
                private String name;
                private String slug;
            }
        }
    }

    @Data
    public static class Meta {
        private Results results;

        @Data
        public static class Results {
            private int total;
        }
    }
}