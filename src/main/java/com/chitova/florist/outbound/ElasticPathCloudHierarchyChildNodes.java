package com.chitova.florist.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElasticPathCloudHierarchyChildNodes {

    private List<NodeData> data;
    private Meta meta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NodeData {
        private String type;
        private String id;
        private Attributes attributes;
        private Relationships relationships;
        private MetaData meta;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Attributes {
            private String description;
            private String name;
            private String slug;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Relationships {
            private Children children;
            private Products products;

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Children {
                private List<Object> data;  // Replace Object with actual type if known
                private Links links;

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class Links {
                    private String related;
                }
            }

            @Data
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Products {
                private List<Object> data;  // Replace Object with actual type if known
                private Links links;

                @Data
                @NoArgsConstructor
                @AllArgsConstructor
                public static class Links {
                    private String related;
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

            @JsonProperty("hierarchy_id")
            private String hierarchyId;

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
