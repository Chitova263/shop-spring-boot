package com.chitova.florist.outbound.checkout.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ElasticPathCloudBulkAddItemsToCartRequest {
    private List<Item> data;
    private Options options;

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item {
        private String type;
        private String id;
        private String sku;
        private String name;
        private String description;
        private Integer quantity;
        private Price price;
        private String code;
    }

    @Data
    @Builder
    public static class Price {
        private int amount;

        @JsonProperty("includes_tax")
        private boolean includesTax;
    }

    @Data
    @Builder
    public static class Options {
        @JsonProperty("add_all_or_nothing")
        private boolean addAllOrNothing;
    }
}
