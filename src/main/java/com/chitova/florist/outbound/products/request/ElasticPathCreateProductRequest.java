package com.chitova.florist.outbound.products.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ElasticPathCreateProductRequest {

    private DataWrapper data;

    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DataWrapper {
        private String type;
        private Attributes attributes;

        @Data
        @Builder
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Attributes {
            private String name;
            private String slug;
            private String sku;
            private String description;
            private String status;

            @JsonProperty("commodity_type")
            private String commodityType;

            private String mpn;

            @JsonProperty("upc_ean")
            private String upcEan;
        }
    }
}