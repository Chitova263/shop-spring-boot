package com.chitova.florist.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MockProduct {

    private DataWrapper data;

    @Data
    public static class DataWrapper {
        private String type;
        private Attributes attributes;

        @Data
        public static class Attributes {
            private String name;
            private String slug;
            private String sku;
            private String description;

            @JsonProperty("shortDescription")
            private String shortDescription;

            @JsonProperty("imageUrl")
            private String imageUrl;

            private String status;

            @JsonProperty("commodity_type")
            private String commodityType;

            private String mpn;

            @JsonProperty("upc_ean")
            private String upcEan;
        }
    }


}
