package com.chitova.florist.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElasticPathCloudHierarchiesResponse {

    private List<DataItem> data;
    private Meta meta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataItem {
        private String type;
        private String id;
        private Attributes attributes;
        private Relationships relationships;
        private MetaData meta;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Attributes {
            private String name;
            private String slug;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Relationships {
            private Children children;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Children {
                private List<Object> data; // You can replace Object with proper class if known
                private Links links;

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class Links {
                    private String self;
                    // If you want "related" instead, annotate similarly
                    // private String related;
                }
            }
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class MetaData {
            private List<Breadcrumb> breadcrumbs;

            @JsonProperty("created_at")
            private Instant createdAt;

            @JsonProperty("has_children")
            private boolean hasChildren;

            private String owner;

            @JsonProperty("updated_at")
            private Instant updatedAt;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Breadcrumb {
                private String id;
                private String name;
                private String slug;
            }
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {
        private Results results;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Results {
            private int total;
        }
    }
}
